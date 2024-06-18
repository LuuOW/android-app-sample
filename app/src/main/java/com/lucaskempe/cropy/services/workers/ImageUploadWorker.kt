package com.lucaskempe.cropy.services.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.BuildConfig
import com.lucaskempe.cropy.services.models.ImageResponse
import com.lucaskempe.cropy.services.network.RetrofitClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class ImageUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val baseUrl: String = BuildConfig.BASE_URL
    }

    val newContext = context

    val image = inputData.getString("image")!!
    val filename = inputData.getString("filename")!!
    val userId = inputData.getString("userId")!!

    override suspend fun doWork(): Result {
        fun uriToFile(context: Context, uri: Uri): File? {
            val contentResolver = context.contentResolver
            val tempFile = File.createTempFile("temp_image", null, context.cacheDir)

            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            return tempFile
        }

        val requestFile = uriToFile(newContext, Uri.parse(image))!!.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(body = requestFile, name = "image", filename = filename)

        val filenamePart = filename.toRequestBody("text/plain".toMediaTypeOrNull())
        val userPart = userId.toRequestBody("text/plain".toMediaTypeOrNull()) // Replace with the actual user ID


        val client = RetrofitClient(context = newContext, baseURL = baseUrl)
        val call = client.imageAPI.uploadImage(imagePart,filenamePart,userPart)
        call.enqueue(object: Callback<ImageResponse> {

            override fun onResponse(p0: Call<ImageResponse>, p1: Response<ImageResponse>) {
                Log.d("IMAGE_API", "SUCCESS")
            }

            override fun onFailure(p0: Call<ImageResponse>, p1: Throwable) {
                Log.d("IMAGE_API", "Fail, ${p1.message} , ${p1.cause}, ${p1.stackTrace}")
            }

        })

        return Result.success()
    }

}