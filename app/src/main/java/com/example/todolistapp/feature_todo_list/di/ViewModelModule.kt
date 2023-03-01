package com.example.todolistapp.feature_todo_list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolistapp.feature_todo_list.presentation.todo_editor.TodoEditorViewModel
import com.example.todolistapp.feature_todo_list.presentation.todo_list.TodoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    internal abstract fun todoListViewModel(viewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoEditorViewModel::class)
    internal abstract fun todoEditorModel(viewModel: TodoEditorViewModel): ViewModel
}