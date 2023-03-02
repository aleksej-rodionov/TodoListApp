package com.example.todolistapp.feature_todo_list.presentation.todo_list

import com.example.todolistapp.feature_todo_list.domain.model.Todo

sealed class ItemModel {
    data class TodoItem(
        val text: String,
        val isCompleted: Boolean,
        val id: Int? = null
    )

    data class Divider(
        val isCompletedDisplayed: Boolean
    )
}

fun Todo.toTodoItem(): ItemModel.TodoItem {
    return ItemModel.TodoItem(
        text = this.text,
        isCompleted = this.isCompleted,
        id = this.id
    )
}

fun ItemModel.TodoItem.toTodo(): Todo {
    return Todo(
        text = this.text,
        isCompleted = this.isCompleted,
        id = this.id
    )
}
