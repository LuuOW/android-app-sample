package com.lucaskempe.cropy.services.network.token

import android.content.Context
import com.lucaskempe.cropy.providers.data.UserSessionDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object TokenUtil {



    suspend fun retrieveToken(context: Context): String {
        return withContext(Dispatchers.IO) {
            try {
                context.UserSessionDataStore.data
                    .first() // Get the first emission (assuming a Flow from DataStore)
                    .token   // Extract the token
            } catch (e: Exception) {
                // Handle exceptions (e.g., no data, invalid token)
                // You might log the error, throw a custom exception, or return a default value.
                "DEFAULT_TOKEN" // Example: Replace with your error handling logic.
            }
        }
    }


}