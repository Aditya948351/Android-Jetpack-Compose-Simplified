package com.example.isllearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.isllearningapp.ui.theme.GreenJC
import com.example.isllearningapp.ui.theme.Pink40

class SubjectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Subjects(navController = rememberNavController())
        }
    }
}

@Composable
fun Subjects(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Select Subject", fontSize = 30.sp, color = GreenJC)

            // Display subjects here
            SubjectSelection()
        }

        // Back Arrow
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Pink40
            )
        }
    }
}

@Composable
fun SubjectSelection() {
    // Create buttons or any UI element for subject selection
    Column {
        Button(onClick = { /*Add that here*/ }) {
            Text("Subject 1")
        }
        Button(onClick = { /* Handle Subject 2 */ }) {
            Text("Subject 2")
        }
        Button(onClick = { /* Handle Subject 3 */ }) {
            Text("Subject 3")
        }
    }
}
