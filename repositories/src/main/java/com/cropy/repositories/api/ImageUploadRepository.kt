package com.cropy.repositories.api

import android.net.Uri

interface ImageUploadRepository {

    suspend fun uploadImage(uri: Uri, filename: String, userId: String): Result<Boolean>

}