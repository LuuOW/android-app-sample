package com.lucaskempe.cropy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.ui.screens.items.ButtonGrid
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
    val openAlertDialog = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxSize()
    ) {
        ButtonGrid(vm = vm, modifier = modifier, sheetState = sheetState)
    }

    if (sheetState.isVisible) {

        val scrollState = rememberScrollState()

        ModalBottomSheet(
            onDismissRequest = {
                if (vm.modalButtonState.value != ButtonState.SAVE) {
                    vm.modalButtonState.value = ButtonState.EDIT
                } else {
                    openAlertDialog.value = true
                }
            },
            sheetState = sheetState,
            content = {
                Column(
                    modifier //.verticalScroll(scrollState)
                ) {

                    Button(onClick = {
                        when (vm.modalButtonState.value) {
                            ButtonState.EDIT -> {
                                vm.modalButtonState.value = ButtonState.CANCEL
                            }

                            ButtonState.SAVE -> {
                                vm.modalButtonState.value = ButtonState.EDIT
                                scope.launch {
                                    sheetState.hide()
                                }
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

                    ButtonGrid(vm = vm, modifier = modifier, sheetState = sheetState)
                    Spacer(modifier = modifier.padding(bottom = 150.dp))
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
