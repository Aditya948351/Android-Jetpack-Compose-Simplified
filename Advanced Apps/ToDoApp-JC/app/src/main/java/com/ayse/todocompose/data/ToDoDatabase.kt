package com.ayse.todocompose.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDAO(): ToDoDao
}