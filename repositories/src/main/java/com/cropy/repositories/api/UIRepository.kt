package com.cropy.repositories.api

import com.cropy.entities.ui.UIModel


interface UIRepository {

    suspend fun fetchUIById(id: String): Result<UIModel>

    suspend fun fetchAllUI(): Result<List<UIModel>>

}