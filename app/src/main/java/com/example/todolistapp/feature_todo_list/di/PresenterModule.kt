package com.example.todolistapp.feature_todo_list.di

import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.AlarmUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.SetAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.TodoUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.UpdateTodo
import com.example.todolistapp.feature_todo_list.presentation.todo_editor.TodoEditorPresenter
import com.example.todolistapp.feature_todo_list.presentation.todo_list.TodoListPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideTodoListPresenter(
        alarmUseCases: AlarmUseCases,
        todoUseCases: TodoUseCases
    ): TodoListPresenter {
        return TodoListPresenter(alarmUseCases, todoUseCases)
    }

    @Provides
    fun provideTodoEditorPresenter(
        alarmUseCases: AlarmUseCases,
        todoUseCases: TodoUseCases
    ): TodoEditorPresenter {
        return TodoEditorPresenter(alarmUseCases, todoUseCases)
    }
}