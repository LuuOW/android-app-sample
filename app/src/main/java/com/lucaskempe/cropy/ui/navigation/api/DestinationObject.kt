package com.lucaskempe.cropy.ui.navigation.api

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DestinationObject(
    //@SerializedName("graph_name")
    var graphName: String,
    //@SerializedName("start_graph_destination")
    var startGraphDestiny: String,
    //@SerializedName("composable_destination")
    val composables: MutableMap<String, @Composable () -> Unit> = mutableMapOf()
)