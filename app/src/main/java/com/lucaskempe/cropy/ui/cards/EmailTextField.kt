package com.lucaskempe.cropy.ui.cards

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun ValidatingInputTextField(
    inputValue: String,
    updateState: (String) -> Unit,
    validatorHasErrors: Boolean,
    field: String
) {
    OutlinedTextField(
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
