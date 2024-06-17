package com.lucaskempe.cropy.ui.views

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomSearch(
    vm: UsersViewModel,
    drawerState: DrawerState
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(percent = 100)
            ),
        value = vm.userQuery,
        onValueChange = {
            vm.updateQuery(it)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (vm.userQuery.text.isNotEmpty()) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }
        ),
        trailingIcon = if (vm.userQuery.text.isNotEmpty()) {
            @Composable {
                IconButton(onClick = {
                    vm.updateQuery(TextFieldValue(""))
                }) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        } else {
            @Composable {}
        },
        leadingIcon = if (true) {
            //vm.userQuery.text.isNotEmpty()
            @Composable {
                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isOpen) drawerState.close() else drawerState.open()
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Search")
                }
            }
        } else {
            @Composable{}
        }
    )

}
