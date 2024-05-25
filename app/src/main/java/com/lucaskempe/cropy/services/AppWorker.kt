package com.lucaskempe.cropy.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.network.api.LoginRequest
import com.lucaskempe.cropy.services.network.api.LoginResponse
import com.lucaskempe.cropy.services.repository.LocalDataSourceImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


@HiltWorker
class AppWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val imageUri = inputData.getString("imageUri")
    private val retrofitClient = RetrofitClient.loginAPI

    private val localDataSource = LocalDataSourceImpl(context)

    val imageBitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(Uri.parse(imageUri)))

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, outputStream)
        val byteArray = outputStream.toByteArray()
        outputStream.close()
        return byteArray
    }


    override fun doWork(): Result {

        val byteArray = bitmapToByteArray(imageBitmap)

        CoroutineScope(Dispatchers.IO + Job()).launch {

            val db = Firebase.firestore


            // upload to Firebase FIrestore Database to the product collection
  /*          val storageRef = Firebase.storage.reference.child("images/my-image.jpg")
            storageRef.putBytes(byteArray)
                .addOnSuccessListener {
                    Log.d("image", "Image uploaded successfully")
                }
                .addOnFailureListener {
                    Log.d("image", "Image upload failed")
                }
*/

            fun convertBitmapToFile(context: Context, imageBitmap: Bitmap): File {
                // Create a temporary file
                val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)

                // Write the bitmap to the file
                tempFile.outputStream().use {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }

                // Return the temporary file
                return tempFile
            }

            val imageFile = convertBitmapToFile(context = this@AppWorker.applicationContext, imageBitmap = imageBitmap)
            val imageBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

            val imagePart = MultipartBody.Part.createFormData(
                name = "image",
                filename = imageFile.name,
                body = imageBody
            )

            val loginCredentials = LoginRequest("lkemdaspe", "123dasda45")

            CoroutineScope(Dispatchers.IO + Job()).launch {
                retrofitClient.login(loginCredentials).enqueue(object : Callback<String> {


                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            val token = loginResponse // Handle the received token
                            Log.d("login", "Token: $token")
                        } else {
                            // Handle login errors (e.g., invalid credentials)
                            val errorBody = response.errorBody()?.string()
                            Log.d("login", "Error: $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        // Handle network errors
                        Log.d("login", "Error: ${t.message}")
                    }
                })
            }

        }

        return Result.success()
    }



}
