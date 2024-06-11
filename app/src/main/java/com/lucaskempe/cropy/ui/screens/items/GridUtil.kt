package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

class GridUtil {


    fun calculateDistance(index: Int, list: MutableList<ButtonData>, queue: Int = 0): IntOffset {
        val currentRow = index / 4
        val currentColumn = index % 4

        val destination = list.indexOfFirst { it.icon.isEmpty() } + queue

        val destinationRow = destination / 4
        val destinationColumn = destination % 4

        val dx = (destinationColumn - currentColumn) * 264
        val dy = (destinationRow - currentRow) * 264

        return IntOffset(dx, dy) // Adjust vertical offset as needed
    }

    fun calculateOffset(
        data: ButtonData,
        shiftIndex: Int,
        firstIndex: Int
    ): IntOffset {

        val previous = data.previousOffset.value
        val current = data.offSet.value

        if (!isHorizontal(current, previous, shiftIndex, firstIndex)) {
            val rowOffset = 792 * shiftIndex
            val columnOffset = 264 * shiftIndex
            return IntOffset(rowOffset, -columnOffset)
        } else {
            val rowOffset = 264 * shiftIndex
            return IntOffset(-rowOffset, 0)
        }

    }

    fun isHorizontal(currentOffset: IntOffset, previousOffset: IntOffset, iteration: Int, initialPosition: Int): Boolean {

        val xDiff = abs(currentOffset.x - previousOffset.x)
        val yDiff = abs(currentOffset.y - previousOffset.y)

        val expectedHorizontalDiff = 264 * (iteration - 1)

        return if (iteration == 1 && initialPosition % 4 == 0) {
            false // first iteration
        } else if ( currentOffset.x % 264 == 0 && currentOffset.y % 264 == 0  ) {
            true // horizontal
        } else {
            false // not horizontal (diagonal)
        }

    }

    fun emptyOffset(index: Int): IntOffset {
        val column = index % 4

        val size = 80.dp
        val padding = 8.dp

        if (column == 2) {
            val rowOffset = 9 * (size.value + padding.value).roundToInt()
            val columnOffset = 3 * (size.value + padding.value).roundToInt()
            return IntOffset(-rowOffset, columnOffset)
        } else {
            val rowOffset = -3 * (size.value + padding.value).toInt()
            val columnOffset = 0 * (size.value + padding.value).toInt()
            return IntOffset(-rowOffset, columnOffset)
        }
    }
}


