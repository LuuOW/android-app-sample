package com.lucaskempe.cropy.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cropy.datastore.ui.UIDataStore
import com.cropy.entities.ui.UIModel

@Composable
fun CodeVerificationScreen(
    onSubmitCode: (code: String) -> Unit = {},
) {

    var splashData by remember { mutableStateOf(UIModel()) }
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.UIDataStore.data.collect { it.getOrNull(0)?.let { splashData = it } }
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = splashData.model_description,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace,
            textDecoration = TextDecoration.Underline
        )

        Spacer(Modifier.padding(vertical = 10.dp))

        VerificationCodeField(onFilled = {
            code = it
        })

        Spacer(Modifier.padding(vertical = 10.dp))

        Row {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier,
                onClick = {
                    onSubmitCode(code)
                }) {
                Text(
                    text = splashData.model_buttons_titles.first(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Monospace,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun VerificationCodeField(
    length: Int = 6,
    onFilled: (code: String) -> Unit = { }
) {
    val focusRequesters: List<FocusRequester> = remember { List(length) { FocusRequester() } }
    var codeList by remember { mutableStateOf(List(length) { "" }) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(length) { index ->
            OutlinedTextField(
                value = codeList[index],
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        val updatedCodeList = codeList.toMutableList()
                        updatedCodeList[index] = newValue
                        codeList = updatedCodeList.toList()

                        // Handle focus and completion
                        if (newValue.isNotEmpty() && index < length - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (codeList.all { it.isNotEmpty() }) {
                            onFilled(codeList.joinToString(""))
                        }
                        // Handle erasing:
                        else if (newValue.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .padding(4.dp)
                    .focusRequester(focusRequesters[index]),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors().copy(focusedTextColor = Color.White),
                textStyle = TextStyle(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}

@Composable
@Preview
fun CodeVerificationScreenPreview() {
    CodeVerificationScreen()
}


class MaskTransformation(val mask: CharArray) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = mask.copyOf()
        for (i in text.indices) {
            out[i] = text[i]
        }
        return TransformedText(AnnotatedString(out.concatToString()), OffsetMapping.Identity)
    }
}
