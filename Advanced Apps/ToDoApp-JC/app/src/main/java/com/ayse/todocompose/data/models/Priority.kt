package com.ayse.todocompose.data.models

import androidx.compose.ui.graphics.Color
import com.ayse.todocompose.ui.theme.HighPriorityColor
import com.ayse.todocompose.ui.theme.LowPriorityColor
import com.ayse.todocompose.ui.theme.MediumPriorityColor
import com.ayse.todocompose.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}