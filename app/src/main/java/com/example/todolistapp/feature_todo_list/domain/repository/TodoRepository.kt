package com.example.todolistapp.feature_todo_list.domain.repository

import com.example.todolistapp.feature_todo_list.domain.model.Todo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TodoRepository {

    fun insertTodo(todo: Todo): Single<Long>

    fun updateTodo(todo: Todo): Completable

    fun deleteTodo(todo: Todo): Completable

    fun getTodoById(id: Int): Single<Todo>

    fun getAllTodos(): Flowable<List<Todo>>
}