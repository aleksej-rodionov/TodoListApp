package com.example.todolistapp.feature_todo_list.data.repository

import com.example.todolistapp.feature_todo_list.data.local.TodoDao
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoReposiroty
import io.reactivex.Flowable
import io.reactivex.Single

class TodoRepositoryImpl(
    private val todoDao: TodoDao
): TodoReposiroty {

    override fun insertTodo(todo: Todo) {
        TODO("Not yet implemented")
    }

    override fun updateTodo(todo: Todo) {
        TODO("Not yet implemented")
    }

    override fun deleteTodo(todo: Todo) {
        TODO("Not yet implemented")
    }

    override fun getTodoById(id: Int): Single<Todo> {
        TODO("Not yet implemented")
    }

    override fun getAllTodos(): Flowable<List<Todo>> {
        TODO("Not yet implemented")
    }
}