@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tfmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tfmodel.ui.theme.TFModelTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TFModelTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ImageUploaderScreen()
                }
            }
        }
    }
}

// ✅ UI for Image Upload & OCR with Animated Colors
@Composable
fun ImageUploaderScreen() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var extractedText by remember { mutableStateOf("") }
    var mistralResponse by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val buttonColor by animateColorAsState(if (isProcessing) Color.Gray else Color.Magenta)

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start=16.dp,end=16.dp,top=46.dp, bottom = 16.dp)
    ) {
        item {
            Button(
                onClick = { imagePicker.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Image")
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        selectedImageUri?.let { uri ->
            item {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        isProcessing = true
                        processImage(context, uri) {
                            extractedText = it
                            isProcessing = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Extract Text (OCR)")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (extractedText.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Extracted Text:",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = extractedText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(extractedText))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("Copy Text", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        isProcessing = true
                        sendImageToMistral(context, selectedImageUri!!) {
                            mistralResponse = it
                            isProcessing = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Upload & Process Image")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (mistralResponse.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Blue)
                ) {
                    Text(
                        text = "Mistral Response:\n$mistralResponse",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ✅ Sending Image to Mistral API
fun sendImageToMistral(context: Context, imageUri: Uri, onResult: (String) -> Unit) {
    val apiUrl = "https://mistralocrai-production.up.railway.app/process"
    val client = OkHttpClient()

    try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "image", "uploaded_image.jpg",
                inputStream!!.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Mistral API", "Error: ${e.message}")
                onResult("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    onResult(responseBody)
                } ?: onResult("Error: Empty Response")
            }
        })
    } catch (e: Exception) {
        Log.e("Image Upload", "Error: ${e.message}")
        onResult("Error: ${e.message}")
    }
}
