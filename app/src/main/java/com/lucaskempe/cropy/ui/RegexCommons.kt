package com.lucaskempe.cropy.ui

import androidx.compose.ui.text.input.TextFieldValue

fun emailHasErrors(email: TextFieldValue): Boolean {
    return if (email.text.isNotEmpty()) {
        // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
    } else {
        false
    }
}

fun passwordHasError(password: TextFieldValue): Boolean {
    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
    return if (password.text.isNotEmpty()) {
        !password.text.matches(passwordRegex)
    } else {
        false
    }
}
