package com.lucaskempe.cropy.ui.screens.items

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.OffsetEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel

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

    var showDropBoxes by remember { mutableStateOf(fButtons.size < 7) } // Flag for Drop Boxes

    val plusBoxRow = fButtons.size / 4
    val plusBoxColumn = fButtons.size % 4

    val scope = rememberCoroutineScope()

    val offset by remember {
        mutableStateOf(Offset(100f,100f))
    }

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
                        Box(modifier = modifier.clickable {
                            if(vm.modalButtonState.value == ButtonState.SAVE || vm.modalButtonState.value == ButtonState.CANCEL) {
                                vm.gButtons.add(fButtons[index])
                                vm.fButtons.remove(fButtons[index])
                                vm.modalButtonState.value = ButtonState.SAVE
                            }
                        }){
                            fButtons[index]().apply {
                                if (vm.modalButtonState.value == ButtonState.SAVE || vm.modalButtonState.value == ButtonState.CANCEL) {
                                    Icon(
                                        Icons.Filled.KeyboardArrowDown,
                                        contentDescription = "Remove", // Accessibility
                                        modifier = Modifier
                                            .offset { offset.round() }
                                            .shadow(elevation = 50.dp, shape = AbsoluteRoundedCornerShape(100), clip = true)
                                            .clickable {
                                                Log.d("IconClick", "You're hitting click buttonnC")
                                            }
                                            .size(20.dp), // Adjust the size as needed
                                        tint = Color.Red // Customize the color
                                    )
                                }
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
                            Box(modifier = modifier.clickable {
                                if(vm.modalButtonState.value == ButtonState.SAVE || vm.modalButtonState.value == ButtonState.CANCEL) {
                                    vm.fButtons.add(gButtons[index])
                                    vm.gButtons.remove(gButtons[index])
                                    vm.modalButtonState.value = ButtonState.SAVE
                                }
                            }){
                                gButtons[index]().apply {
                                    if (vm.modalButtonState.value == ButtonState.SAVE || vm.modalButtonState.value == ButtonState.CANCEL) {
                                        Icon(
                                            Icons.Filled.KeyboardArrowUp,
                                            contentDescription = "Add", // Accessibility
                                            modifier = Modifier
                                                .size(20.dp), // Adjust the size as needed
                                            tint = Color.Green // Customize the color
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}