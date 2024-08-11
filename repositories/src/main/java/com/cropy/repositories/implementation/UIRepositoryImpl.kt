package com.cropy.repositories.implementation

import android.content.Context
import com.cropy.datastore.ui.UIDataStore
import com.cropy.entities.ui.UIModel
import com.cropy.network.api.ServerDrivenUIAPI
import com.cropy.repositories.api.UIRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UIRepositoryImpl @Inject constructor(
    private val uiAPI: ServerDrivenUIAPI,
    @ApplicationContext private val context: Context
): UIRepository {

    override suspend fun fetchUIById(id: String): Result<UIModel> {
        val uiResult = uiAPI.getUIById(id)
        if (uiResult.isSuccessful && uiResult.body() != null) {
            val splash = uiResult.body()
            return Result.success(splash!!)
        } else {
            return Result.failure(Throwable("Error in splash ui request"))
        }
    }

    override suspend fun fetchAllUI(): Result<List<UIModel>> {
        val call = uiAPI.getAllUI()
        val result = call.body()
        if (call.isSuccessful && result != null) {
            context.UIDataStore.updateData { result }
            return Result.success(result)
        } else {
            return Result.failure(Throwable("Error in repository requesting all ui"))
        }
    }

}