package com.example.todolistapp.feature_todo_list.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoEntity(
    val text: String,
    val isCompleted: Boolean = false,
    val needShowReminder: Boolean = false,
    @PrimaryKey val id: Int? = null
)
