


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.lucaskempe.cropy.BuildConfig
import java.io.File
import java.io.FileOutputStream

@Composable
fun SelectImageScreen(
    selectedImage: Uri,
    context: Context,
    imageDescription: MutableState<String>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

            Image(
                bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(selectedImage)).asImageBitmap(),
                contentDescription = "Compressed Image",
                modifier = Modifier.fillMaxSize()
            )

            LaunchedEffect(
                key1=selectedImage,
                key2=imageDescription
            ) {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-pro-vision",
                    apiKey = BuildConfig.GL_API_KEY
                )

                val image: Bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(selectedImage))
                val inputContent = content() {
                    image(image)
                    text("What's your opinion about the habitat of the animal in this image? in five words")
                }
                val response = generativeModel.generateContent(inputContent)
                imageDescription.value = response.text.toString()
            }
        }
}

@Composable
fun loadBitmapFromUri(context: android.content.Context, uri: Uri): ImageBitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}

fun compressAndSaveImage(context: Context, uri: Uri): Uri {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val file = File(context.cacheDir, "compressed_image.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
    outputStream.close()

    // Returning a content URI for the compressed image file
    return FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file
    )
}