package com.lucaskempe.cropy.ui.navigation.api

import androidx.compose.runtime.Composable

class RouterImplementation(
    private val destinationSet: MutableSet<DestinationObject>
): RouterDestinity {


    override fun addToGraph(
        graphName: String,
        destination: @Composable () -> Unit,
        composableRoute: String
    ) {
        destinationSet.find { it.graphName == graphName }?.composables?.put(composableRoute,  destination)
    }

    override fun createGraph(
        graphName: String,
        startGraphDestiny: String,
        destinationList: MutableMap<String, @Composable () -> Unit>
    ) {
        destinationSet.add(DestinationObject(
            graphName=graphName,
            startGraphDestiny = startGraphDestiny,
            composables = destinationList
        ))
    }

}