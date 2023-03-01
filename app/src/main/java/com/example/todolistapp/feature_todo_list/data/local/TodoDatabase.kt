package com.example.todolistapp.feature_todo_list.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolistapp.feature_todo_list.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun dao(): TodoDao
}