package com.lucaskempe.cropy.providers

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_data")
data class ImageData(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val imagePath: String,
    val url: String
)