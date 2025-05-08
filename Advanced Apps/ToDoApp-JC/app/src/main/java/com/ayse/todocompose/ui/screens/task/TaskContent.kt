package com.ayse.todocompose.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ayse.todocompose.R
import com.ayse.todocompose.components.PriorityDropDown
import com.ayse.todocompose.data.models.Priority
import com.ayse.todocompose.ui.theme.LARGE_PADDING
import com.ayse.todocompose.ui.theme.MEDIUM_PADDING

@Composable
fun TaskContent (
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(all = LARGE_PADDING)
            .background(MaterialTheme.colors.background)
            .padding(all = LARGE_PADDING)
    ){
        OutlinedTextField(
            modifier= Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title))},
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color= MaterialTheme.colors.background

        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        OutlinedTextField(
            modifier= Modifier.fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(R.string.description))},
            textStyle = MaterialTheme.typography.body1,
        )
    }
}
@Composable
@Preview
private fun TaskContentPreview(){
    TaskContent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}