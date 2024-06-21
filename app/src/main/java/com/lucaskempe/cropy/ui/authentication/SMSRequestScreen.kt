package com.lucaskempe.cropy.ui.authentication

import android.graphics.Shader
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.snackbar.Snackbar
import com.lucaskempe.cropy.R
import com.lucaskempe.cropy.providers.data.SplashUIModel
import com.lucaskempe.cropy.services.workers.MailRequestWorker
import com.lucaskempe.cropy.services.workers.SMSRequestWorker

@Composable
fun SMSRequestScreen(
    vm: AuthViewmodel,
    navController: NavController
) {

    val workManager: WorkManager = WorkManager.getInstance(vm.context)

    val largeRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(Color(0xFF9FA8DA), Color(0xFF000000)),
                center = size.center,
                radius = biggerDimension / 2f,
                colorStops = listOf(0f, 0.95f)
            )
        }
    }

    val view = LocalView.current

    var splashData by remember { mutableStateOf(SplashUIModel()) }


    val vectorPainter = rememberVectorPainter(
        defaultWidth = 530.46f.dp,
        defaultHeight = 563.1f.dp,
        viewportWidth = 530.46f,
        viewportHeight = 563.1f,
        autoMirror = true,
    ) { viewPortWidth, viewPortHeight ->

    }

    Image(
        vectorPainter,
        contentDescription = "Jellyfish",
        modifier = Modifier
            .fillMaxSize()
            .background(largeRadialGradient)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
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

        PhoneNumberPicker(vm)

        Spacer(Modifier.padding(vertical = 10.dp))

        Row {
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp),
                onClick = {
                    if (true) {
                        val workRequest = OneTimeWorkRequest.Builder(SMSRequestWorker::class.java)
                            .setInputData(workDataOf("phone" to (vm.selectedCountry.value.dialCode.plus(vm.phoneNumber.value))))
                            .build()
                        workManager.enqueueUniqueWork("one_time", ExistingWorkPolicy.KEEP, workRequest)
                        navController.navigate("verification")
                    } else {
                        Snackbar
                            .make(view, "Invalid phone number, please enter a valid number", Snackbar.LENGTH_SHORT)
                            .setActionTextColor(ContextCompat.getColor(vm.context, R.color.white))
                            .setBackgroundTint(ContextCompat.getColor(vm.context, R.color.notred))
                            .show()
                    }
                }
            ) {
                Text(
                    text = splashData.model_button_title,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberPicker(
    vm: AuthViewmodel
) {
    var showCountryPicker by remember { mutableStateOf(false) }

    val countries = listOf(
        Country("UR", "Uruguay", "+59", "🇺🇾"),
        Country("AR", "Argentina", "+54", "🇦🇷"),
        Country("CA", "Canada", "+1", "🇨🇦"),
        Country("US", "United States", "+1", "🇺🇸")
    )

    // Styling for the OutlinedTextField
    val outlinedTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.White, // Blue border when focused
        unfocusedBorderColor = Color.Gray, // Gray border when not focused
        cursorColor = Color.White,
    )

    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = vm.phoneNumber.value,
        onValueChange = { vm.phoneNumber.value = it },
        label = { Text("Phone Number", color = Color.White) }, // Dark gray label
        modifier = Modifier.width(300.dp),
        leadingIcon = {
            Row(
                modifier = Modifier
                    .clickable { showCountryPicker = !showCountryPicker }
                    .padding(8.dp)
            ) {
                Text(vm.selectedCountry.value.flag, fontSize = 18.sp, color = Color.White) // Slightly larger flag
                Spacer(modifier = Modifier.width(8.dp))
                Text(vm.selectedCountry.value.dialCode, fontSize = 16.sp, color = Color.White) // Slightly larger dial code
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.White) // Gray icon
            }
        },
        colors = outlinedTextFieldColors
    )

    if (showCountryPicker) {
        AlertDialog(
            onDismissRequest = { showCountryPicker = false },
            title = { Text("Select Country") },
            text = {
                var searchQuery by remember { mutableStateOf("") }

                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CountryPicker(
                        countries.filter { it.name.contains(searchQuery, ignoreCase = true) },
                        onCountrySelected = { country ->
                            vm.selectedCountry.value = country
                            showCountryPicker = false
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showCountryPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CountryPicker(countries: List<Country>, onCountrySelected: (Country) -> Unit) {
    LazyColumn {
        items(countries) { country ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCountrySelected(country) }
                    .padding(8.dp)
            ) {
                Text(country.flag, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(country.dialCode, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(country.name, fontSize = 16.sp)
            }
        }
    }
}

data class Country(val code: String, val name: String, val dialCode: String, val flag: String)
