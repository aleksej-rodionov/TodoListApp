package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoReposiroty
import com.example.todolistapp.feature_todo_list.presentation.util.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val reposiroty: TodoReposiroty //todo useCase/interactor
): ViewModel() {

    private val compDisp = CompositeDisposable()

    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

    private val _uiEffect: MutableLiveData<Event<UiEffect>> = MutableLiveData()
    val uiEffect: LiveData<Event<UiEffect>> = _uiEffect

    init {
        observeAllTodos()
    }

    private fun observeAllTodos() {
        val output = reposiroty.getAllTodos()
        output.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _todos.value = it
            }, {
                Log.d("TAG_TODO_LIST", "observeAllTodos: error = ${it.message}")
            }).apply { compDisp.add(this) }
    }

    fun onCompletedChanged(todo: Todo, completed: Boolean) {
        //todo
    }

    fun onTodoClick(todo: Todo) {
        _uiEffect.value = Event(UiEffect.OnNavigateEditTodo(todo))
    }

    fun onAddTodoClick() {
        _uiEffect.value = Event(UiEffect.OnNavigateAddTodo)
    }

    override fun onCleared() {
        super.onCleared()
        compDisp.clear()
    }
}

sealed class UiEffect {
    data class OnNavigateEditTodo(val todo: Todo): UiEffect()
    object OnNavigateAddTodo: UiEffect()
}