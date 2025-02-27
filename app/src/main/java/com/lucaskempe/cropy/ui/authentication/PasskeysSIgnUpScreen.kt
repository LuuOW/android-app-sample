package com.lucaskempe.cropy.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cropy.components.fonts.AppTypography
import com.cropy.components.topbar.AppTopBar
import com.lucaskempe.cropy.R

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
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth()
        ) {

            Text(text = "Sign Up", style = AppTypography.titleL)

            Spacer(Modifier.size(40.dp))

            Text(text = "Full name", style = AppTypography.bodyM)

            TextField(value = fullName, onValueChange = { input -> fullName = input }, modifier = Modifier, shape = RoundedCornerShape(25.dp))

            Spacer(Modifier.size(20.dp))

            Text(text = "Username", style = AppTypography.bodyM)

            TextField(value = username, onValueChange = { input -> username = input }, modifier = Modifier, shape = RoundedCornerShape(25.dp))
            Spacer(Modifier.size(20.dp))

            Text(text = "Signing In", style = AppTypography.bodyM)
            Spacer(Modifier.size(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(140.dp)
            ) {

                Row(
                    modifier = Modifier.size(400.dp).padding(end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        Text(
                            modifier = Modifier
                                .width(250.dp),
                            text =
                            buildAnnotatedString {
                                append(stringResource(R.string.signing_in_with_passkeys))
                                pushStyle(SpanStyle(fontWeight = FontWeight.Bold)) // Start bold style
                                append(" How passkeys work")
                                pop() // End bold style
                            }
                        )
                        Text("Other ways to sign in", style = AppTypography.bodyM)

                    }

                }

            }




        }

    }

}

// Example usage:


@Preview(showBackground = true)
@Composable
fun PasskeysSignUpScreenPreview() {
    PasskeysSignUpScreen()
}