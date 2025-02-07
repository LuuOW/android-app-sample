package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.lucaskempe.cropy.R


@Composable
fun ValidatingPasswordTextField(
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(focusedTextColor = Color.White),
    inputValue: TextFieldValue,
    updateState: (TextFieldValue) -> Unit,
    validatorHasErrors: Boolean,
    isRepeat: Boolean
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility) R.drawable.ic_password_visible else R.drawable.ic_password_hidden

    OutlinedTextField(
        colors = colors,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        value = inputValue,
        onValueChange = updateState,
        label = { Text("Password", color = Color.White) },
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
            IconButton(onClick = { passwordVisibility = !passwordVisibility }, colors = IconButtonDefaults.iconButtonColors().copy(contentColor = Color.White)) {
                Icon(painter = painterResource(id = icon), contentDescription = "Toggle password visibility")
            }
        }
    )
}

@Composable
@Preview
fun ValidatingPasswordTextFieldPreview() {
    ValidatingPasswordTextField(inputValue = TextFieldValue(""), updateState = { TextFieldValue("") }, validatorHasErrors = false, isRepeat = false)
}