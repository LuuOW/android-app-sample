package com.cropy.repositories.implementation

import android.content.Context
import android.net.Uri
import com.cropy.network.api.ImagesAPI
import com.cropy.repositories.api.ImageUploadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imagesAPI: ImagesAPI
): ImageUploadRepository {

    override suspend fun uploadImage(uri: Uri, filename: String, userId: String): Result<Boolean> {
        val requestFile = uriToFile(context, uri)!!.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", filename, requestFile)

        val filenamePart = filename.toRequestBody("text/plain".toMediaTypeOrNull())
        val userPart = userId.toRequestBody("text/plain".toMediaTypeOrNull()) // Replace with the actual user ID

        val callResult = imagesAPI.uploadImage(imagePart, filenamePart, userPart)
        val callData = callResult.body()
        if (callResult.isSuccessful && callData != null) {
            return Result.success(true)
        } else {
            return Result.failure(Throwable("Failure in image upload repository"))
        }

    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp_image", null, context.cacheDir)

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return tempFile
    }

}