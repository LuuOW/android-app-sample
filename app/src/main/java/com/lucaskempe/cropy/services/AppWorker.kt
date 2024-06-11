package com.lucaskempe.cropy.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.repository.LocalDataSourceImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File


@HiltWorker
class AppWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val imageUri = inputData.getString("imageUri")
    private val retrofitClient = RetrofitClient.bgEraserAPI

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

            val file = File(imageUri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imageFilePart = MultipartBody.Part.createFormData("image_file", file.name, requestFile)

            val sizePart = "auto".toRequestBody("text/plain".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO + Job()).launch {
                try {
                    val result = retrofitClient.removeBackground(imageFilePart,sizePart)
                } catch (e: Exception) {
                    throw Exception("Error uploading image, cause: ${e.message}, ${e.cause}")
                }
            }

        }

        return Result.success()
    }



}
