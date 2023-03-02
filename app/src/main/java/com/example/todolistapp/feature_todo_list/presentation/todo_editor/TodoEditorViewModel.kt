package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoReposiroty
import com.example.todolistapp.feature_todo_list.presentation.util.Event
import javax.inject.Inject

private const val TAG = "TodoEditorViewModel"

class TodoEditorViewModel @Inject constructor(
    private val reposiroty: TodoReposiroty //todo useCase/interactor
): ViewModel() {

    var todoId: Int? = null
    private var todoCompleted: Boolean? = false

    private val _todoText: MutableLiveData<String> = MutableLiveData()
    val todoText: LiveData<String> = _todoText

    private val _uiEffect: MutableLiveData<Event<UiEffect>> = MutableLiveData()
    val uiEffect: LiveData<Event<UiEffect>> = _uiEffect

    fun setTodo(todo: Todo?) {
        Log.d(TAG, "setTodo: called")
        todoId = todo?.id
        todoCompleted = todo?.isCompleted
        updateText(todo?.text)
    }

    fun updateText(text: String?) {
        text?.let { _todoText.value = it }
    }

    fun onSaveClick() {
        if (todoId == null) {
            val newTodo = Todo(
                text = todoText.value ?: "",
                isCompleted = false
            )
            reposiroty.insertTodo(newTodo)
        } else {
            val updatedTodo = Todo(
                text = todoText.value ?: "",
                isCompleted = todoCompleted ?: false,
                id = todoId
            )
            reposiroty.updateTodo(updatedTodo)
        }

//        _uiEffect.value = Event(UiEffect.NavigateBack)
    }

    fun deleteTodo() {
        val todo = Todo(
            text = todoText.value ?: "",
            isCompleted = todoCompleted ?: false,
            id = todoId
        )
        reposiroty.deleteTodo(todo)
    }

    sealed class UiEffect {
        object NavigateBack: UiEffect()
    }
}
