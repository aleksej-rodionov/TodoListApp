package com.example.todolistapp.feature_todo_list.domain.use_case

import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository

class UpdateTodo(
    private val reposiroty: TodoRepository
) {

    operator fun invoke(todo: Todo) {
        reposiroty.updateTodo(todo)
    }
}