package com.lucaskempe.cropy.ui.screens.items

data class ButtonGrid(
    val rows : Int,
    val columns : Int,
    val buttons: Map<Int, ButtonData>,
    val placeHolder: Map<Int, Int>,
)

data class ButtonStructure(
    val positionX: Int,
    val positionY: Int,
    val isVisible: Boolean,
)

fun ButtonGrid.getButtonStructure() : Map<Int, ButtonStructure> {
    val buttonStructure = mutableMapOf<Int, ButtonStructure>()
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            val index = i * columns + j
            if (index < buttons.size) {
                buttonStructure[index] = ButtonStructure(i, j, true)
            } else {
                buttonStructure[index] = ButtonStructure(i, j, false)
            }
        }
    }
    return buttonStructure
}