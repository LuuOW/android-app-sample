package com.lucaskempe.cropy.ui.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cropy.components.fonts.AppTypography
import com.cropy.components.topbar.AppTopBar

@Composable
fun PasskeysSignUpScreen(
    onBackPressed: () -> Unit = {}
) {

    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppTopBar(onLeadingIconPressed = { onBackPressed() })

        Column(
            modifier = Modifier.padding(start = 15.dp)
        ) {

            Text(text = "Sign Up", style = AppTypography.titleL)

            Spacer(Modifier.size(40.dp))

            Text(text = "Full name", style = AppTypography.bodyM)

            TextField(value = fullName, onValueChange = { input -> fullName = input }, modifier = Modifier, shape = RoundedCornerShape(25.dp))

            Spacer(Modifier.size(20.dp))

            Text(text = "Username", style = AppTypography.bodyM)

            TextField(value = username, onValueChange = { input -> username = input }, modifier = Modifier, shape = RoundedCornerShape(25.dp))

        }

    }

}

@Preview(showBackground = true)
@Composable
fun PasskeysSignUpScreenPreview() {
    PasskeysSignUpScreen()
}