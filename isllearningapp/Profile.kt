package com.example.isllearningapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

@Composable
@Preview
fun Profile() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

    // Retrieve saved user data
    val savedEmail = sharedPreferences.getString("username", "No Username") ?: "No Username"
    val savedUserType = sharedPreferences.getString("userType", "No User Type") ?: "No User Type"
    var username by remember { mutableStateOf(savedEmail) }
    var name by remember { mutableStateOf(sharedPreferences.getString("name", "No Name") ?: "No Name") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditingUsername by remember { mutableStateOf(false) }

    // Retrieve profile image URL from SharedPreferences
    var profileImageUrl by remember { mutableStateOf(sharedPreferences.getString("profileImageUrl", null)) }

    // Launchers for gallery, camera, and permission requests
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        profileImageUri = uri
        // Save the image URI locally
        sharedPreferences.edit().putString("profileImageUri", uri.toString()).apply()
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) profileImageUri = profileImageUri
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (!granted) Log.d("ProfileScreen", "Permission denied for camera")
    }

    // Temporary file for camera images
    val imageFile = File(context.filesDir, "profile_image.jpg")
    val imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)

    // Layout
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Profile", fontSize = 30.sp, color = Color.Green)
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image
            profileImageUrl?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray)
                )
            } ?: profileImageUri?.let {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray)
                )
            } ?: Image(
                painter = rememberImagePainter(R.drawable.handshake),
                contentDescription = "Default Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Button to choose an image
            IconButton(
                onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        galleryLauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.background(Color.Yellow)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile Image")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display email and user type
            Text(text = "Email: $savedEmail", fontSize = 20.sp, color = Color.Green)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "User Type: $savedUserType", fontSize = 20.sp, color = Color.Green)

            // Editable Username
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isEditingUsername) {
                    BasicTextField(
                        value = username,
                        onValueChange = { if (it.all { char -> char.isLetterOrDigit() || char.isWhitespace() }) username = it },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                } else {
                    Text(text = "Username: $username", fontSize = 20.sp, color = Color.Green)
                }
                IconButton(onClick = { isEditingUsername = !isEditingUsername }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Username")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            Button(
                onClick = {
                    // Save the data, including the profile image URL if available
                    saveDataToFirebase(username, name, savedUserType, profileImageUri, sharedPreferences)
                },
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Log Out button
            Button(
                onClick = { logOut(context, sharedPreferences) },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Log Out")
            }
        }
    }
}

// Save data to Firebase and SharedPreferences
fun saveDataToFirebase(username: String, name: String, userType: String, profileImageUri: Uri?, sharedPreferences: SharedPreferences) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val userId = UUID.randomUUID().toString()

    val profileImageUrl = profileImageUri?.let { uri ->
        uploadProfileImage(uri,
            onSuccess = { imageUrl ->
                val user = User(username, name, userType, imageUrl)
                usersRef.child(userId).setValue(user)
                    .addOnSuccessListener {
                        Log.d("ProfileScreen", "User data saved successfully with image URL")
                        sharedPreferences.edit().apply {
                            putString("username", username)
                            putString("name", name)
                            putString("profileImageUrl", imageUrl)
                            apply()
                        }
                    }
                    .addOnFailureListener { Log.w("ProfileScreen", "Error saving user data", it) }
            },
            onFailure = {
                Log.w("ProfileScreen", "Image upload failed")
            })
    }
}

// Upload profile image to Firebase Storage
fun uploadProfileImage(uri: Uri, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val profileImageRef = storageRef.child("profile_images/${UUID.randomUUID()}.jpg")

    profileImageRef.putFile(uri)
        .addOnSuccessListener { taskSnapshot ->
            profileImageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onSuccess(downloadUri.toString())
            }
        }
        .addOnFailureListener {
            onFailure()
        }
}

// Log out function
fun logOut(context: Context, sharedPreferences: SharedPreferences) {
    sharedPreferences.edit().clear().apply()
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
    if (context is Activity) context.finish()
}

// User data model
data class User(val username: String = "", val name: String = "", val userType: String = "", val profileImageUrl: String = "")
