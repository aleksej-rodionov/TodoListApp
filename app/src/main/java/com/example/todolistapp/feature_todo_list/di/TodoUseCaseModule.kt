package com.example.todolistapp.feature_todo_list.di

import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TodoUseCaseModule {

    @Provides
    @Singleton
    fun provideTodoUseCases(repository: TodoRepository): TodoUseCases {
        return TodoUseCases(
            insertTodo = InsertTodo(repository),
            updateTodo = UpdateTodo(repository),
            deleteTodo = DeleteTodo(repository),
            getTodoById = GetTodoById(repository),
            getAllTodos = GetAllTodos(repository)
        )
    }
}