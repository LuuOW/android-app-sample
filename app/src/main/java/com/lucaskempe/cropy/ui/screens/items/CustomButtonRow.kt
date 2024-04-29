package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CustomButtonRow(
    modifier: Modifier,
    navController: NavHostController,
    vm: UsersViewModel,
    mSheetState: SheetState
) {

/*    val fButtons = vm.fButtons
    var gButtons = vm.gButtons
    val sButtons = vm.sButtons*/

    val fButtons = vm.fIconList
    val gButtons = vm.gIconList
    val sButtons = vm.sIconList


    val editing = vm.modalButtonState.value != ButtonState.EDIT
    var placeholderCount = 0

    val editingConditionList = !mSheetState.isVisible || mSheetState.isVisible && editing

    var moved by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val newOffset = remember {
        mutableStateOf(IntOffset(0,0))
    }

    var size by remember { mutableStateOf(IntSize.Zero) }

    val pxToMove = with(LocalDensity.current) {
        80.dp.toPx().roundToInt() + 8.dp.toPx().roundToInt() +
                24.dp.toPx().roundToInt() + 15.dp.toPx().roundToInt()
    }

    val offset by animateIntOffsetAsState(
        animationSpec = tween(3000, delayMillis = 500),
        targetValue = if (moved) {
            newOffset.value
        } else {
            IntOffset.Zero
        },
        label = "offset"
    )

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
                                    modifier
                                        .offset { offset }
                                        .clickable {
                                            vm.gIconList.add(fButtons[index])
                                            vm.fIconList.remove(fButtons[index])
                                            vm.modalButtonState.value = ButtonState.SAVE
                                        }
                                } else {
                                    modifier
                                }
                            ) {
                                CustomButton(
                                    navController = navController,
                                    vm=vm,
                                    data = fButtons[index],
                                ).apply { if (editing) RemoveIconButton() }
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
            repeat((gButtons.size + 3) / 4) { rowIndex ->
                Row {
                    repeat(4) { columnIndex ->
                        val index = rowIndex * 4 + columnIndex
                        if (index < gButtons.size) {
                            Box(modifier = if (editing) {
                                modifier
                                    .onSizeChanged { size = it }
                                    .onGloballyPositioned {
                                        val offset = it.positionInRoot()
                                        newOffset.value = IntOffset(
                                            x = offset.x.roundToInt(),
                                            y = offset.y.roundToInt()
                                        )
                                    }
                                    .clickable {
                                        vm.fIconList.add(gButtons[index])
                                        vm.gIconList.remove(gButtons[index])
                                        vm.modalButtonState.value = ButtonState.SAVE
                                    }
                            } else {
                                modifier
                            }) {
                                CustomButton(
                                    navController = navController,
                                    vm=vm,
                                    data = gButtons[index]
                                ).apply {
                                    IncludeIconButton()
                                }
                            }
                        }
                    }
                }
            }
        } else {
            repeat((sButtons.size + 3) / 4) { rowIndex ->
                Row {
                    repeat(4) { columnIndex ->
                        val index = rowIndex * 4 + columnIndex
                        if (index < sButtons.size) {
                            Box(modifier = modifier) {
                                CustomButton(navController = navController, vm=vm, data = sButtons[index])
                            }
                        }
                    }
                }
            }
        }
    }
}