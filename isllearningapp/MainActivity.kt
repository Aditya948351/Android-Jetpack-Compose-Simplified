@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.isllearningapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.isllearningapp.R
import com.example.isllearningapp.ui.theme.GreenJC
import com.example.isllearningapp.ui.theme.ISLLearningAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}

        //Adding a delay
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000L)
            splashScreen.setKeepOnScreenCondition{false}
        }
        setContent {
            ISLLearningAppTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                )  {
                    LearnNavBotSheet()
                }
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun LearnNavBotSheet() {
    val navigationcontroller = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext

    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Box(modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFA500),
                                Color.White,
                                Color.Green
                            )
                        )
                    )
                    .fillMaxWidth()
                    .height(260.dp)) {
                    Column(modifier = Modifier.padding(start = 85.dp,top = 54.dp)) {
                        Text(
                            text = "Math Feedback Tracker",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Profile Image (Handshake.png)
                        Image(
                            painter = painterResource(id = R.drawable.handshake),  // Replace with your image resource
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(80.dp)  // Adjust the size as needed
                                .clip(CircleShape)  // Circle shape for image
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Email Address
                        Text(
                            text = "ap8548328@gmail.com",  // Your static email address
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                Divider()
                NavigationDrawerItem(label = { Text(text = "Home", color = Color.White) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "home", tint = GreenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "My Learnings", color = GreenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Build, contentDescription = "My Learnings", tint = GreenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.MyLearnings.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "Subjects", color = GreenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.MailOutline, contentDescription = "Subjects", tint = GreenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.Subjects.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "AiChat", color = GreenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Ai Chat", tint = GreenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.AiChat.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "About Us", color = GreenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Info, contentDescription = "About Us", tint = GreenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.AboutUs.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "Help", color = GreenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Info, contentDescription = "Help", tint = GreenJC)},
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.Help.screen) {
                            popUpTo(0)
                        }
                    })
                NavigationDrawerItem(label = { Text(text = "Profile", color = GreenJC) },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = GreenJC) },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationcontroller.navigate(Screens.Profile.screen) {
                            popUpTo(0)
                        }
                    })
            }
        },
    ) {
        Scaffold(
            topBar = {
                val coroutineScope = rememberCoroutineScope()
                TopAppBar(
                    title = { Text("ISLLearningApp") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GreenJC,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Rounded.Menu, contentDescription = "MenuButton")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = GreenJC) {
                    // Left side: First two icons (Home and Edit)
                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Home
                            navigationcontroller.navigate(Screens.Home.screen) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                        )
                    }

                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Edit
                            navigationcontroller.navigate(Screens.MyLearnings.screen) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = if (selected.value == Icons.Default.Edit) Color.White else Color.DarkGray
                        )
                    }

                    // Center: Floating Action Button
                    Box(modifier = Modifier
                        .weight(2f)  // Takes up more space for the FAB
                        .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center) {
                        FloatingActionButton(
                            onClick = { showBottomSheet = true },
                            containerColor = Color.White
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = GreenJC)
                        }
                    }

                    // Right side: Last two icons (DateRange and Person)
                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.DateRange
                            navigationcontroller.navigate(Screens.Exercise.screen) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = if (selected.value == Icons.Default.DateRange) Color.White else Color.DarkGray
                        )
                    }

                    IconButton(
                        onClick = {
                            selected.value = Icons.Default.Person
                            navigationcontroller.navigate(Screens.Profile.screen) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = if (selected.value == Icons.Default.Person) Color.White else Color.DarkGray
                        )
                    }
                }
            }
        ) {
            NavHost(navController = navigationcontroller,
                startDestination = Screens.Home.screen) {
                composable(Screens.Home.screen){ Home() }
                composable(Screens.Learn.screen){ Learn() }
                composable(Screens.Exercise.screen){ Exercises() }
                composable(Screens.Profile.screen){ Profile() }
                composable(Screens.MyLearnings.screen){ MyLearnings() }
                composable(Screens.Help.screen){ Help() }
                composable(Screens.AboutUs.screen){ AboutUs() }
                composable(Screens.SignToSpeech.screen){ SignToSpeech() }
                composable(Screens.Subjects.screen) { Subjects(navController = navigationcontroller) }
                composable(Screens.AiChat.screen) { AiChat() }
                composable(Screens.Practice.screen) { Practice() }
            }
        }
    }
}