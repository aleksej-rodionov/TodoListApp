package com.example.todolistapp.feature_todo_list.data.local.mapper

import com.example.todolistapp.feature_todo_list.data.local.entity.TodoEntity
import com.example.todolistapp.feature_todo_list.domain.model.Todo

fun TodoEntity.toTodo(): Todo {
    return Todo(
        text,
        isCompleted,
        id
    )
}

fun Todo.toTodoEntity(): TodoEntity {
    return TodoEntity(
        text,
        isCompleted,
        id
    )
}