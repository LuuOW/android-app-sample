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
import com.google.firebase.app
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.lucaskempe.cropy.services.network.RetrofitClient
import com.lucaskempe.cropy.services.repository.LocalDataSourceImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@HiltWorker
class AppWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val imageUri = inputData.getString("imageUri")
    private val retrofitClient = RetrofitClient.uploadImage()

    private val localDataSource = LocalDataSourceImpl(context)

    val imageBitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(Uri.parse(imageUri)))

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
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


 /*           val response = retrofitClient.uploadImage(imageByteArray)
            if (response.isSuccessful) {
                val imageResponse = response.body()?.url
                // local data source save
            } else {
                throw IllegalArgumentException("Name cannot be blank")
            }*/
        }

        return Result.success()
    }



}
