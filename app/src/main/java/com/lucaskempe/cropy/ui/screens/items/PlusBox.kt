package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlusBox(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: UsersViewModel,
    mSheetState: SheetState
    ) {

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .shadow(4.dp, AbsoluteRoundedCornerShape(8.dp))
    ) {
        IconButton(
            modifier = modifier
                .background(color = Color.White)
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)) ,
            onClick = {
                scope.launch {
                    mSheetState.expand()
                }
            }
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "add more"
            )
        }
    }
}