package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomButtonRow(
    modifier: Modifier,
    navController: NavHostController,
    vm: UsersViewModel,
    mSheetState: SheetState
) {

    val fButtons = vm.fButtons
    val gButtons = vm.gButtons

    val editing = vm.modalButtonState.value == ButtonState.SAVE || vm.modalButtonState.value == ButtonState.CANCEL

    var showDropBoxes by remember { mutableStateOf(fButtons.size < 7) } // Flag for Drop Boxes

    val plusBoxRow = fButtons.size / 4
    val plusBoxColumn = fButtons.size % 4

    val scope = rememberCoroutineScope()


    val interactionSource = remember { MutableInteractionSource() }

    var fMoved by remember { mutableStateOf(false) }

    val pxToMove = with(LocalDensity.current) {
        100.dp.toPx().roundToInt()
    }


    val gPositionOffset = remember {
        mutableStateOf(Offset(0f,0f))
    }

    val fOffset by animateIntOffsetAsState(
        targetValue = if (fMoved) {
            gPositionOffset.value.round()
        } else {
            IntOffset.Zero
        },
        label = "F row offset"
    )



    Column(modifier = modifier) {
        val totalItems = fButtons.size + if (showDropBoxes) 7 - fButtons.size else 0
        val rows = (totalItems + 3) / 4 // Calculate rows (rounding up)

        repeat(rows) { rowIndex ->
            Row {
                repeat(4) { columnIndex ->
                    if (rowIndex == plusBoxRow && columnIndex == plusBoxColumn && !mSheetState.isVisible) {
                        PlusBox(navController = navController, vm = vm, mSheetState = mSheetState)
                    }
                    val index = rowIndex * 4 + columnIndex
                    if (index < fButtons.size) {
                        Box(modifier = modifier
                            .offset { fOffset }
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if (editing) {
                                    fMoved = !fMoved
                                    vm.gButtons.add(fButtons[index])
                                    vm.fButtons.remove(fButtons[index])
                                    vm.modalButtonState.value = ButtonState.SAVE
                                }
                            }){
                            fButtons[index]().apply {
                                if (editing) RemoveIconButton()
                            }
                        }
                    } else if (showDropBoxes && mSheetState.isVisible) {
                        PlaceHolderBox(modifier = modifier)
                    }
                }
            }
        }

        if (mSheetState.isVisible) {
            repeat(2) {rowIndex ->
                Row {
                    repeat(4) {columnIndex ->
                        val index = rowIndex*4+columnIndex
                        if (index < gButtons.size) {
                            Box(modifier = modifier
                                .onGloballyPositioned { coordinates ->
                                    val x = coordinates.positionInRoot().x
                                    val y = (coordinates.positionInRoot().y / 1.5).toFloat()
                                    gPositionOffset.value = Offset(x,y)
                                }
                                .clickable {
                                    if (editing) {
                                        vm.fButtons.add(gButtons[index])
                                        vm.gButtons.remove(gButtons[index])
                                        vm.modalButtonState.value = ButtonState.SAVE
                                    }
                                }){
                                gButtons[index]().apply { if (editing) IncludeIconButton() }
                            }
                        }
                    }
                }
            }
        }
    }
}