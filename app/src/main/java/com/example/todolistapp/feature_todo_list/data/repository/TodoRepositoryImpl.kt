package com.example.todolistapp.feature_todo_list.data.repository

import com.example.todolistapp.feature_todo_list.data.local.TodoDao
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodo
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodoEntity
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoReposiroty
import io.reactivex.Flowable
import io.reactivex.Single

class TodoRepositoryImpl(
    private val todoDao: TodoDao
): TodoReposiroty {

    override fun insertTodo(todo: Todo) {
        todoDao.insertTodo(todo.toTodoEntity())
    }

    override fun updateTodo(todo: Todo) {
        todoDao.updateRodo(todo.toTodoEntity())
    }

    override fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo.toTodoEntity())
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