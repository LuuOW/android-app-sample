package com.lucaskempe.cropy.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.runBlocking

class ImageContentProvider : ContentProvider() {

    private val databaseInitializer: DatabaseInitializer by lazy {
        val appContext = context?.applicationContext ?: throw IllegalStateException("Context not available")
        DatabaseInitializer.getInstance(appContext)
    }

    private val imageDao: ImageDao
        get() = databaseInitializer.imageDao

    override fun onCreate(): Boolean {
        // ... other provider initialization logic
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = values?.getAsLong("id") ?: 1
        val imagePath = values?.getAsString("imagePath") ?: ""
        val url = values?.getAsString("url") ?: ""

        val image = ImageData(id, imagePath, url)
        runBlocking { imageDao.insertImage(image) }
        return Uri.parse(imagePath)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("id", "name", "url")) // Define column names

        val images = runBlocking { imageDao.getAllImages() }
        for (image in images) {
            cursor.addRow(arrayOf(image.id, image.imagePath, image.url))
        }

        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val id = uri.getQueryParameter("id")?.toLong() ?: 1
        val imagePath = uri.getQueryParameter("imagePath") ?: ""
        val url = uri.getQueryParameter("url") ?: ""
        val imageToDelete = ImageData(id, imagePath, url)
        return runBlocking {
            imageDao.deleteImage(imageToDelete)
        }
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {


        val id = contentValues?.getAsLong("id") ?: 1
        val imagePath = contentValues?.getAsString("imagePath") ?: ""
        val url = contentValues?.getAsString("url") ?: ""

        val image = ImageData(id, imagePath, url)
        return runBlocking {
            imageDao.updateImages(image)
        }
    }

    override fun getType(uri: Uri): String? {
        return null // We're not going to use MIME types in this example
    }
}
