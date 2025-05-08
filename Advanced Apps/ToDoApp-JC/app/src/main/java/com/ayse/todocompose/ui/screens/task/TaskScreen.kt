package com.ayse.todocompose.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ayse.todocompose.data.ToDoTask
import com.ayse.todocompose.data.models.Priority
import com.ayse.todocompose.ui.viewModel.SharedViewModel
import com.ayse.todocompose.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask : ToDoTask?,
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
){
    val title : String = sharedViewModel.title
    val description : String = sharedViewModel.description
    val priority : Priority = sharedViewModel.priority

   val context = LocalContext.current

    BackHandler {
        Log.d("BACK HANDLER", "Triggered")
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold (
        topBar = {
            TaskAppBar(
            navigateToListScreen = { action ->
                if (action == Action.NO_ACTION){
                    navigateToListScreen(action)
                }else {
                    if (sharedViewModel.validateFields()){
                        navigateToListScreen(action)
                    }else{
                        displayToast(context = context )
                    }
                }

            } ,
            selectedTask = selectedTask
        )
                 },
        content = {
            TaskContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.updateDescription(newDescription = it)
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.updatePriority(newPriority = it)
                }
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(context, "Fields Empty.", Toast.LENGTH_SHORT).show()
}
