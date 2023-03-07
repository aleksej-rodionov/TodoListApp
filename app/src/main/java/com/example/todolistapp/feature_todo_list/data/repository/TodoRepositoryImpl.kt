package com.example.todolistapp.feature_todo_list.data.repository

import android.annotation.SuppressLint
import com.example.todolistapp.feature_todo_list.data.local.TodoDao
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodo
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodoEntity
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

private const val TAG = "TodoRepositoryImpl"

@SuppressLint("CheckResult")
class TodoRepositoryImpl(
    private val todoDao: TodoDao
) : TodoRepository {

    override fun insertTodo(todo: Todo): Single<Long> {
        return todoDao.insertTodo(todo.toTodoEntity())
            .subscribeOn(Schedulers.io())
    }

    override fun updateTodo(todo: Todo): Completable = todoDao.updateTodo(todo.toTodoEntity())
        .subscribeOn(Schedulers.io())

    override fun deleteTodo(todo: Todo): Completable = todoDao.deleteTodo(todo.toTodoEntity())
        .subscribeOn(Schedulers.io())

    override fun getTodoById(id: Int): Single<Todo> {
        return todoDao.getTodoById(id).map {
            it.toTodo()
        }.subscribeOn(Schedulers.io())
    }

    override fun getAllTodos(): Flowable<List<Todo>> {
        return todoDao.getAllTodos()
            .map { entityList ->
                entityList.map {
                    it.toTodo()
                }
            }.subscribeOn(Schedulers.io())
    }
}