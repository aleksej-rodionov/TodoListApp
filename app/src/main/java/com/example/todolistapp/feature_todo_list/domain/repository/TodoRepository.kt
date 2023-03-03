package com.example.todolistapp.feature_todo_list.domain.repository

import com.example.todolistapp.feature_todo_list.domain.model.Todo
import io.reactivex.Flowable
import io.reactivex.Single

interface TodoRepository {

    fun insertTodo(todo: Todo)

    fun updateTodo(todo: Todo)

    fun deleteTodo(todo: Todo)

    fun getTodoById(id: Int): Single<Todo>

    fun getAllTodos(): Flowable<List<Todo>>
}