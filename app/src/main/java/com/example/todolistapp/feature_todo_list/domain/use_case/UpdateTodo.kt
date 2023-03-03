package com.example.todolistapp.feature_todo_list.domain.use_case

import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM

class UpdateTodo(
    private val reposiroty: TodoRepository
) {

    operator fun invoke(todo: Todo) {
        Log.d(TAG_ALARM, "UpdateTodo.invoke: id = ${todo.id}, isCompleted = ${todo.isCompleted}")
        reposiroty.updateTodo(todo)
    }
}