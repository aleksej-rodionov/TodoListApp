package com.example.todolistapp.feature_todo_list.domain.use_case.todo

import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository

class DeleteTodo(
    private val repository: TodoRepository
) {

    operator fun invoke(todo: Todo) = repository.deleteTodo(todo)
}