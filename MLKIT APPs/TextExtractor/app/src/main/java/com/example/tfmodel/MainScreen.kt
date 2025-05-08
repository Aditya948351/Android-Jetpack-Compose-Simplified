package com.example.tfmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun MainScreen(bitmap: Bitmap) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    var extractedText by remember { mutableStateOf("") }

    LaunchedEffect(bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { extractedText = it.text }
            .addOnFailureListener { extractedText = "Failed to recognize text" }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Extracted Text:")
        Text(extractedText, modifier = Modifier.padding(top = 8.dp))
    }
}

