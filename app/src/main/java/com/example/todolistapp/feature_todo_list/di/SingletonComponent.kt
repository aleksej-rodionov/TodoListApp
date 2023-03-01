package com.example.todolistapp.feature_todo_list.di

import android.app.Application
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import com.example.todolistapp.feature_todo_list.presentation.todo_editor.TodoEditorFragment
import com.example.todolistapp.feature_todo_list.presentation.todo_list.TodoListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [

    ]
)
interface SingletonComponent {

    fun inject(application: TodoListApp)
    fun inject(activity: MainActivity)
    fun inject(fragment: TodoListFragment)
    fun inject(fragment: TodoEditorFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): SingletonComponent
    }
}