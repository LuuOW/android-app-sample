package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ButtonGrid(
    modifier: Modifier,
    sheetState: SheetState,
    vm: UsersViewModel
) {
    //val vm: UsersViewModel = hiltViewModel()

    val gu = remember { GridUtil() }


    val scope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()
    var list = remember { mutableStateListOf<ButtonData>() }

    val movedIndex = remember {
        mutableStateOf(-1)
    }


    if (vm.modalButtonState.value == ButtonState.EDIT && sheetState.isVisible) {
        list.clear() // Clear existing items
        list.addAll(vm.sIconList)
    } else if (!sheetState.isVisible) {
        list.clear()
        list.addAll(vm.fIconList)
        list.removeIf { it.icon.isEmpty() } // Remove items with empty icons
    } else {
        list.clear()
        list.addAll(vm.fIconList)
        val itemToRemove = vm.fIconList.find { it.id == 7 }
        if (itemToRemove != null) {
            list.remove(itemToRemove) // Remove item by reference
        }
        list.addAll(vm.gIconList)
    }

    val queue = remember {
        mutableStateListOf(0)
    }

    val shiftQueue = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = list) {
        queue.clear()

        val elements = list.filterNot { it.icon.isEmpty() && it.enabled.value }

        queue.addAll(elements.indices)
    }

    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Adaptive(88.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
    ) {
        itemsIndexed(list) { index, item ->
            CustomButton(
                data = item,
                onAnimationFinish = {
                    scope.launch {
                        delay(400)
                        list.forEach {
                            //it.enabled.value = false
                            //it.offSet.value = it.originalOffset.value
                            //queue.add(1)
                        }
                    }
                },
                onClick = {
                    if (item.id == 7) {
                        scope.launch { sheetState.expand() }
                    } else {
                        if (vm.modalButtonState.value != ButtonState.EDIT &&
                            list[index].icon.isNotEmpty() && !list[index].enabled.value
                        ) {
                            val queueIndex = queue.removeFirst()

                            if(queue.isNotEmpty()) {
                                //list[index].offSet.value = gu.calculateDistance(index, list, queueIndex)
                            }

                        }

                    }
                }
            )
        }
    }

}


