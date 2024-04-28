package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CustomButtonRow(
    modifier: Modifier,
    navController: NavHostController,
    vm: UsersViewModel,
    mSheetState: SheetState
) {

    val fButtons = vm.fButtons
    var gButtons = vm.gButtons
    val sButtons = vm.sButtons

    val editing = vm.modalButtonState.value != ButtonState.EDIT
    var placeholderCount = 0

    val editingConditionList = !mSheetState.isVisible || mSheetState.isVisible && editing

    if (editingConditionList) {
        Column(
            modifier = modifier
        ) {
            val totalItems = fButtons.size + if (fButtons.size < 7) 7 - fButtons.size else 0
            val rows = (totalItems + 3) / 4

            repeat(rows)
            { rowIndex ->
                Row {
                    repeat(4) { columnIndex ->
                        if (rowIndex == fButtons.size / 4 && columnIndex == fButtons.size % 4 && !mSheetState.isVisible) {
                            PlusBox(
                                navController = navController,
                                vm = vm,
                                mSheetState = mSheetState
                            )
                        }
                        val index = rowIndex * 4 + columnIndex
                        if (index < fButtons.size) {
                            Box(
                                modifier =
                                if (editing) {
                                    modifier.clickable {
                                        vm.gButtons.add(fButtons[index])
                                        vm.fButtons.remove(fButtons[index])
                                        vm.modalButtonState.value = ButtonState.SAVE
                                    }
                                } else { modifier }
                            ) {
                                fButtons[index]().apply { if (editing) RemoveIconButton() }
                            }
                        } else if (mSheetState.isVisible && placeholderCount < 7 - fButtons.size) {
                            PlaceHolderBox(modifier = modifier)
                            placeholderCount++
                        }
                    }
                }
            }
        }
        Spacer(modifier = modifier.padding(10.dp))
    }

    if (mSheetState.isVisible) {

        if (editing) {
            repeat((gButtons.size + 3 ) / 4) { rowIndex ->
                Row {
                    repeat(4) { columnIndex ->
                        val index = rowIndex * 4 + columnIndex
                        if (index < gButtons.size) {
                            Box(modifier = if (editing) {
                                modifier.clickable {
                                    vm.fButtons.add(gButtons[index])
                                    vm.gButtons.remove(gButtons[index])
                                    vm.modalButtonState.value = ButtonState.SAVE
                                }
                            } else {
                                modifier
                            }) {
                                gButtons[index]().apply { if (editing) IncludeIconButton() }
                            }
                        }
                    }
                }
            }
        } else {
            repeat((sButtons.size + 3 ) / 4) { rowIndex ->
                Row {
                    repeat(4) { columnIndex ->
                        val index = rowIndex * 4 + columnIndex
                        if (index < sButtons.size) {
                            Box(modifier = modifier) {
                                sButtons[index]()
                            }
                        }
                    }
                }
            }
        }
    }
}