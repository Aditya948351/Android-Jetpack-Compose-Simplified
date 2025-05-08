package com.ayse.todocompose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ayse.todocompose.R
import com.ayse.todocompose.data.models.Priority
import com.ayse.todocompose.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.ayse.todocompose.ui.theme.PRIORITY_INDICATOR_SIZE

@Composable
fun PriorityDropDown(
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val angle : Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = "jnj"
    )

    var parentSize by remember { mutableStateOf(IntSize.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { parentSize = it.size }
            .height(PRIORITY_DROP_DOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)){
            drawCircle(color = priority.color)

        }
        Text(
            modifier = Modifier.weight(8f),
            text = priority.name,
            style = MaterialTheme.typography.subtitle1
        )
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(degrees = angle)
                .weight(1.5f),
            onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = stringResource(R.string.drop_down_icon) )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            //scope func with
            modifier = Modifier.width(with(LocalDensity.current) {
                parentSize.width.toDp()
            })
        ) {
            Priority.values().slice(0..2).forEach { priority ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onPrioritySelected(priority)
                }) {
                    PriorityItem(priority = priority)
                }
            }
        }
    }
}

@Composable
@Preview

fun PreviewPriorityDropDown(){
    PriorityDropDown(
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}