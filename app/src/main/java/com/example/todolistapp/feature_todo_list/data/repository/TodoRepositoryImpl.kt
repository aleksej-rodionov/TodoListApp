package com.example.todolistapp.feature_todo_list.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.todolistapp.feature_todo_list.data.local.TodoDao
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodo
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodoEntity
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

private const val TAG = "TodoRepositoryImpl"

@SuppressLint("CheckResult")
class TodoRepositoryImpl(
    private val todoDao: TodoDao
): TodoRepository {

    override fun insertTodo(todo: Todo) {
        Observable.fromCallable {
            Runnable {
                todoDao.insertTodo(todo.toTodoEntity())
            }.run()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "insertTodo: success")
            }, {
                Log.d(TAG, "insertTodo: error = ${it.message}")
            })
    }

    override fun updateTodo(todo: Todo) {
        Observable.fromCallable {
            Runnable {
                todoDao.updateRodo(todo.toTodoEntity())
            }.run()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "updateTodo: success")
            }, {
                Log.d(TAG, "updateTodo: error = ${it.message}")
            })
    }

    override fun deleteTodo(todo: Todo) {
        Observable.fromCallable {
            Runnable {
                todoDao.deleteTodo(todo.toTodoEntity())
            }.run()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "deleteTodo: success")
            }, {
                Log.d(TAG, "deleteTodo: error = ${it.message}")
            })
    }

    override fun getTodoById(id: Int): Single<Todo> {
        return todoDao.getTodoById(id).map {
            it.toTodo()
        }
    }

    override fun getAllTodos(): Flowable<List<Todo>> {
        return todoDao.getAllTodos().map { entityList ->
            entityList.map {
                it.toTodo()
            }
        }
    }
}