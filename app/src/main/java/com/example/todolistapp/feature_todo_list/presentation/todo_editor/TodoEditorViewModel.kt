package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import javax.inject.Inject

class TodoEditorViewModel @Inject constructor(

): ViewModel() {

    private var _todoId: Int? = null

    private val _todoText: MutableLiveData<String> = MutableLiveData()
    val todoText: LiveData<String> = _todoText

    fun setTodo(todo: Todo?) {
        _todoId = todo?.id
        _todoText.value = todo?.text
    }
}