package com.example.todolistapp.feature_todo_list.domain.use_case.todo

import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import io.reactivex.Completable

class UpdateTodo(
    private val reposiroty: TodoRepository
) {

    operator fun invoke(todo: Todo): Completable {
        Log.d(TAG_ALARM, "UpdateTodo.invoke: id = ${todo.id}, isCompleted = ${todo.isCompleted}")
        return reposiroty.updateTodo(todo)
    }
}