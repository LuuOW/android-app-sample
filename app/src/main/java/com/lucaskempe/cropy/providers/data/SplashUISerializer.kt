package com.lucaskempe.cropy.providers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object SplashUISerializer: Serializer<SplashUIModel> {

    private val gson = Gson()
    override val defaultValue: SplashUIModel
        get() = SplashUIModel()

    override suspend fun readFrom(input: InputStream): SplashUIModel {
        return gson.fromJson(input.reader(), SplashUIModel::class.java)
    }

    override suspend fun writeTo(t: SplashUIModel, output: OutputStream) {
        withContext(Dispatchers.IO + Job()) {
            output.write(gson.toJson(t).toByteArray())
        }
    }

}

val Context.SplashUIDataStore: DataStore<SplashUIModel> by dataStore(
    fileName = "splash_ui.pb",
    serializer = SplashUISerializer
)

data class SplashUIModel(
    var model_title: String = "welcome.title",
    var model_description: String = "welcome.description",
    var model_image: String = "",
    val model_buttons_titles: Array<String> = arrayOf("model.button.title_0","model.button.title_1"),
    val model_buttons_images: Array<String> = arrayOf(),
    val model_buttons_actions: Array<String> = arrayOf(),
    var model_primary_color: String = "0xFF2be4dc",
    var model_secondary_color: String = "0xFF243484",
)