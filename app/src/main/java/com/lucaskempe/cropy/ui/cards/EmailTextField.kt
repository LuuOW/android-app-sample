package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ValidatingInputTextField(
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    inputValue: TextFieldValue,
    updateState: (TextFieldValue) -> Unit,
    validatorHasErrors: Boolean,
    field: String
) {
    OutlinedTextField(
        colors = colors,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        value = inputValue,
        onValueChange = updateState,
        label = { Text(field) },
        isError = validatorHasErrors,
        supportingText = {
            if (validatorHasErrors) {
                Text("Incorrect $field format.")
            }
        }
    )
}

@Preview
@Composable
fun ValidatingInputTextFieldPreview() {
    ValidatingInputTextField(inputValue = TextFieldValue(""), updateState = {TextFieldValue("")}, validatorHasErrors =  false, field =  "Email")
}
