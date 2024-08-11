package com.cropy.repositories.implementation

import android.content.Context
import com.cropy.datastore.user.UserSessionDataStore
import com.cropy.entities.network.request.EmailRequest
import com.cropy.entities.network.request.LoginRequest
import com.cropy.entities.network.request.PasswordResetRequest
import com.cropy.entities.network.request.SMSRequest
import com.cropy.entities.network.request.SMSVerifyRequest
import com.cropy.entities.network.request.SignUpRequest
import com.cropy.entities.user.UserSession
import com.cropy.network.NetworkErrorConverter
import com.cropy.network.api.AuthAPI
import com.cropy.repositories.api.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Qualifier

class AuthRepositoryImpl @Inject constructor(
    private val authAPI: AuthAPI,
    private val networkErrorConverter: NetworkErrorConverter,
    @ApplicationContext private val context: Context
): AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Result<String> = try {
        val loginCall = authAPI.login(loginRequest).execute()
        val loginData = loginCall.body()
        if (loginCall.isSuccessful && loginData != null) {
            context.UserSessionDataStore.updateData { UserSession(1 ,loginRequest.username, loginRequest.password, loginData.token) }
            Result.success(loginData.token)
        } else {
            Result.failure(networkErrorConverter.parseError(Throwable("error in login")))
        }
        } catch (e: Throwable) {
            throw networkErrorConverter.parseError(e)
        }


    override suspend fun requestSMSCode(smsRequest: SMSRequest): Result<String> {
        val smsReqCall = authAPI.requestSMS(smsRequest).execute()
        val smsReqRes = smsReqCall.body()
        if (smsReqCall.isSuccessful && smsReqRes != null) {
            return Result.success(smsReqRes.status)
        } else {
            return Result.failure(Throwable("error in auth repository requesting SMS code"))
        }
    }

    override suspend fun verifySMSCode(smsVerifyRequest: SMSVerifyRequest): Result<Boolean> {
        val smsVerifyCall = authAPI.verifySMSCode(smsVerifyRequest).execute()
        val smsVerifyRes = smsVerifyCall.body()
        if (smsVerifyCall.isSuccessful && smsVerifyRes != null) {
            if (smsVerifyRes.isValid) {
                return Result.success(true)
            } else {
                return Result.success(false)
            }
        } else {
            return Result.failure(Throwable("error in auth repository with SMS verification"))
        }
    }

    override suspend fun requestEmailCode(emailRequest: EmailRequest): Result<String> {
        val emailRequestCall = authAPI.requestEmail(emailRequest).execute()
        val emailResponse = emailRequestCall.body()
        val emailStatus = emailResponse?.status
        if (emailStatus != null && emailRequestCall.isSuccessful) {
            return Result.success(emailStatus)
        } else {
            return Result.failure(Throwable("Error in auth repository requesting email code"))
        }
    }

    override suspend fun resetPassword(request: PasswordResetRequest): Result<Boolean> {
        val call = authAPI.resetPassword(request).execute()
        return if (call.isSuccessful) {
            Result.success(true)
        } else {
            Result.success(false)
        }
    }

    override suspend fun signUp(request: SignUpRequest): Result<String> {
        val call = authAPI.signUp(request).execute()
        val response = call.body()
        if (call.isSuccessful && response != null) {
            return Result.success(response.status)
        } else {
            return Result.failure(Throwable("Error in auth repository signing up user"))
        }
    }

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher