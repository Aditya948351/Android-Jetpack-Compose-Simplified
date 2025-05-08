package com.ayse.todocompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ayse.todocompose.data.models.Priority
import com.ayse.todocompose.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoTask (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority
    )