package com.lucaskempe.cropy.ui.navigation.api

import androidx.compose.runtime.Composable

interface  RouterDestinity {

    fun addToGraph(graphName: String, destination: @Composable () -> Unit, composableRoute: String)

    fun createGraph(
        graphName: String,
        startGraphDestiny: String,
        destinationList: MutableMap<String, @Composable () -> Unit>
    )

}