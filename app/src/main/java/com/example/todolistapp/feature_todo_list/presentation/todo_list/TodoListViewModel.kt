package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoReposiroty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val reposiroty: TodoReposiroty //todo useCase/interactor
): ViewModel() {

    private val compDisp = CompositeDisposable()

//    private val notes = mutableListOf<Todo>()
    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

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

    override fun onCleared() {
        super.onCleared()
        compDisp.clear()
    }
}