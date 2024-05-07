package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ButtonGrid(
    vm: UsersViewModel,
    modifier: Modifier,
    sheetState: SheetState
) {
    val editing = vm.modalButtonState.value != ButtonState.EDIT
    val scope = rememberCoroutineScope()


    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(80.dp)
        ,modifier=modifier.fillMaxWidth()
    ) {
        items(if(!editing && sheetState.isVisible) vm.sIconList else if(!sheetState.isVisible) vm.fIconList.sortedBy { it.id } else vm.fIconList.minus(vm.fIconList.find { it.id == 7 })) { data ->

            CustomButton(
                data = data!!,
                onClick = {
                    if(data.id == 7) { scope.launch { sheetState.expand() } }
                    else {
                        if (editing) {
                            data.enabled = !data.enabled
                            vm.modalButtonState.value = ButtonState.SAVE
                            vm.fIconList.find { it.id == data.id }?.let { vm.gIconList.add(it) }
                            vm.fIconList.remove(vm.fIconList.find { it.id == data.id })
                        }
                    }
                }
            )
        }
    }

    if (editing) {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(4),modifier=modifier.fillMaxWidth()) {
            items(vm.gIconList.sortedBy { it.id }) { data ->
                CustomButton(
                    data = data,
                    onClick = {
                        data.enabled = !data.enabled
                        vm.modalButtonState.value = ButtonState.SAVE
                        vm.gIconList.find { it.id == data.id }?.let { vm.fIconList.add(it) }
                        vm.gIconList.remove(vm.gIconList.find { it.id == data.id })
                    }
                )
            }
        }
    }

}