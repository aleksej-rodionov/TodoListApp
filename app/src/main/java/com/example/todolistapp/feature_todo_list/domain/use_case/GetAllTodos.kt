package com.example.todolistapp.feature_todo_list.domain.use_case

import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository

class GetAllTodos(
    private val repository: TodoRepository
) {

    operator fun invoke() = repository.getAllTodos()
}