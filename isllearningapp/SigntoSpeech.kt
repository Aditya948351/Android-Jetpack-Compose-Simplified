package com.example.isllearningapp

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission

@Composable
fun SignToSpeech() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    var gestureText by remember { mutableStateOf("No Gesture Detected") }
    var userInputText by remember { mutableStateOf("") }
    val cameraPermissionGranted = remember { mutableStateOf(false) }

    // Request Camera Permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted.value = isGranted
    }

    LaunchedEffect(Unit) {
        if (checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            cameraPermissionGranted.value = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Row with Help Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { /* Navigate to Help Screen */ }) {
                Text("Help")
            }
        }

        // Text Field for input (Text box at the top)
        TextField(
            value = userInputText,
            onValueChange = { userInputText = it },
            label = { Text("Detected Gesture Text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Camera Preview (reduced size) only if permission is granted
        if (cameraPermissionGranted.value) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .weight(0.5f)  // Further reduced camera preview size
                    .fillMaxWidth()
            ) {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA // Use the front camera

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview
                        )
                    } catch (e: Exception) {
                        Log.e("CameraPreview", "Binding failed", e)
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        } else {
            Text(
                text = "Camera permission required",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Placeholder for gesture recognition logic
        LaunchedEffect(Unit) {
            // Simulate detecting "Ka" gesture
            gestureText = "ક" // Simulate "Ka" in Gujarati
        }

        // Display Detected Gesture Text
        Text(
            text = gestureText,
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
