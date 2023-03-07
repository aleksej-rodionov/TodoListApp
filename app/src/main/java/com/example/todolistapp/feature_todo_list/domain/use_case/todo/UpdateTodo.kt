package com.example.todolistapp.feature_todo_list.domain.use_case.todo

import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import io.reactivex.Completable

class UpdateTodo(
    private val reposiroty: TodoRepository
) {

    operator fun invoke(todo: Todo): Completable {
        return reposiroty.updateTodo(todo)
    }
}