package com.lucaskempe.cropy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.ui.screens.items.CustomButtonRow
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.launch


@ExperimentalMaterial3Api
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BuildScreen(
    navController: NavHostController,
    vm: UsersViewModel,
    modifier: Modifier
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    CustomButtonRow(
        modifier = modifier,
        navController = navController,
        vm = vm,
        mSheetState = sheetState
    )

    if (sheetState.isVisible)
        ModalBottomSheet(
            modifier = modifier
                .size(800.dp),
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            sheetState = sheetState,
            content = {
                Button(onClick = {
                    when(vm.modalButtonState.value) {
                        ButtonState.EDIT -> {
                            vm.modalButtonState.value = ButtonState.CANCEL
                        }
                        ButtonState.SAVE -> {
                            vm.modalButtonState.value = ButtonState.EDIT
                        }
                        else -> {
                            vm.modalButtonState.value = ButtonState.EDIT
                        }
                    }
                }) {
                    Text(when(vm.modalButtonState.value) {
                            ButtonState.EDIT -> "Edit"
                            ButtonState.SAVE -> "Save"
                            else -> "Cancel"
                        })
                }

                // row of 5
                CustomButtonRow(
                    modifier = modifier,
                    navController = navController,
                    vm = vm,
                    mSheetState = sheetState
                )
            }
        )
    }



enum class ButtonState {
    SAVE,
    CANCEL,
    EDIT
}
