package com.example.todolistapp.feature_todo_list.domain.model

data class Todo(
    val text: String,
    val isCompleted: Boolean,
    val id: Int? = null
)
