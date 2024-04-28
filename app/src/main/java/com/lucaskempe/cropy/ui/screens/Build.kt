package com.lucaskempe.cropy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

    if (sheetState.isVisible) {

        val scrollState = rememberScrollState()

        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            sheetState = sheetState,
            content = {
                Column (
                    modifier.verticalScroll(scrollState)
                ){

                    Button(onClick = {
                        when (vm.modalButtonState.value) {
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
                        Text(
                            when (vm.modalButtonState.value) {
                                ButtonState.EDIT -> "Edit"
                                ButtonState.SAVE -> "Save"
                                else -> "Cancel"
                            }
                        )
                    }

                    CustomButtonRow(
                        modifier = modifier,
                        navController = navController,
                        vm = vm,
                        mSheetState = sheetState
                    )
                }
            }
        )
    }
}


enum class ButtonState {
    SAVE,
    CANCEL,
    EDIT
}
