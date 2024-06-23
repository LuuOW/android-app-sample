package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun ValidatingPasswordTextField(
    inputValue: String,
    updateState: (String) -> Unit,
    validatorHasErrors: Boolean,
    isRepeat: Boolean
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility) Icons.Filled.Clear else Icons.Filled.Info

    OutlinedTextField(
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        value = inputValue,
        onValueChange = updateState,
        label = { Text("Password") },
        isError = validatorHasErrors,
        supportingText = {
            if (validatorHasErrors && !isRepeat) {
                Text("Incorrect format, 8 characters min, at least 1 uppercase 1 lowercase, 1 special character (!,@,*)")
            } else if (validatorHasErrors && isRepeat){
                Text(text = "Passwords doesn't match")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = icon, contentDescription = "Toggle password visibility")
            }
        }
    )
}