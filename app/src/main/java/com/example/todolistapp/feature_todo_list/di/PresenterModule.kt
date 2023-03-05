package com.example.todolistapp.feature_todo_list.di

import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.SetAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.UpdateTodo
import com.example.todolistapp.feature_todo_list.presentation.todo_editor.TodoEditorPresenter
import com.example.todolistapp.feature_todo_list.presentation.todo_list.TodoListPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideTodoListPresenter(
        repository: TodoRepository,
        updateTodo: UpdateTodo
    ): TodoListPresenter {
        return TodoListPresenter(repository, updateTodo)
    }

    @Provides
    fun provideTodoEditorPresenter(
        repository: TodoRepository,
        setAlarm: SetAlarm,
        removeAlarm: RemoveAlarm,
        checkIfAlarmSet: CheckIfAlarmSet,
        updateTodo: UpdateTodo
    ): TodoEditorPresenter {
        return TodoEditorPresenter(repository, setAlarm, removeAlarm, checkIfAlarmSet, updateTodo)
    }
}