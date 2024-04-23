package com.lucaskempe.cropy.providers

import android.content.ContentValues
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: ImageData): Long

    @Query("SELECT * FROM image_data")
    suspend fun getAllImages(): List<ImageData>

    @Delete
    suspend fun deleteImage(image: ImageData): Int

    @Update // Add @Update annotation for update functionality
    suspend fun updateImages(image: ImageData): Int
}
