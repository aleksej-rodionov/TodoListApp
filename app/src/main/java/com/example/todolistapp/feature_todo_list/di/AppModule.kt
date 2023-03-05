package com.example.todolistapp.feature_todo_list.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.todolistapp.feature_todo_list.data.local.TodoDatabase
import com.example.todolistapp.feature_todo_list.data.repository.TodoRepositoryImpl
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(app, TodoDatabase::class.java, "todo_database").build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.dao())
    }
}