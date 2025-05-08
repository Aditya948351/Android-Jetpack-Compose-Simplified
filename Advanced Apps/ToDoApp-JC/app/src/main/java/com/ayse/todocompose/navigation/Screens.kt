package com.ayse.todocompose.navigation

import androidx.navigation.NavHostController
import com.ayse.todocompose.util.Action
import com.ayse.todocompose.util.Constants.LIST_SCREEN

class Screens(navHostController: NavHostController) {

    val list: (Int) -> Unit = { taskId ->
        navHostController.navigate(route = "task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navHostController.navigate(route = "list/${action}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}