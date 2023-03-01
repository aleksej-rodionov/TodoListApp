package com.example.todolistapp

import android.app.Application
import com.example.todolistapp.feature_todo_list.di.DaggerSingletonComponent
import com.example.todolistapp.feature_todo_list.di.SingletonComponent

class TodoListApp: Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerSingletonComponent.builder().application(this).build()
        component?.inject(this)
    }

    companion object {
        var component: SingletonComponent? = null
    }
}