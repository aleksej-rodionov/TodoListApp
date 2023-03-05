package com.example.todolistapp.feature_todo_list.di

import android.content.Context
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideSetAlarm(context: Context): SetAlarm {
        return SetAlarm(context)
    }

    @Provides
    @Singleton
    fun provideRemoveAlarm(context: Context): RemoveAlarm {
        return RemoveAlarm(context)
    }

    @Provides
    @Singleton
    fun provideCheckIfAlarmSet(context: Context): CheckIfAlarmSet {
        return CheckIfAlarmSet(context)
    }

    @Provides
    @Singleton
    fun provideUpdateTodo(repository: TodoRepository): UpdateTodo {
        return UpdateTodo(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTodo(repository: TodoRepository): DeleteTodo {
        return DeleteTodo(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllTodos(repository: TodoRepository): GetAllTodos = GetAllTodos(repository)

    @Provides
    @Singleton
    fun provideGetTodoById(repository: TodoRepository): GetTodoById = GetTodoById(repository)

    @Provides
    @Singleton
    fun provideInsertTodo(repository: TodoRepository): InsertTodo = InsertTodo(repository)
}