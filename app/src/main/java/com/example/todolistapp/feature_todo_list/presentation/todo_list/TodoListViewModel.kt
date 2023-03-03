package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.UpdateTodo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "TodoListViewModel"

class TodoListViewModel @Inject constructor(
    private val reposiroty: TodoRepository, //todo useCase/interactor
    private val updateTodo: UpdateTodo
) : ViewModel() {

    private val compDisp = CompositeDisposable()

    private var showCompleted = false
    private val todoItems = mutableListOf<ItemModel.TodoItem>()

    private val _todos = MutableLiveData<List<ItemModel>>()
    val todos: LiveData<List<ItemModel>> = _todos

    init {
        observeAllTodos()
    }

    private fun observeAllTodos() {
        val output = reposiroty.getAllTodos()
        output.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                todoItems.clear()
                todoItems.addAll(mapEntriesToTodoItems(it))
                _todos.value = mapEntriesByShowCompleted(todoItems)
            }, {
                Log.d("TAG_TODO_LIST", "observeAllTodos: error = ${it.message}")
            }).apply { compDisp.add(this) }
    }

    fun onCompletedChanged(todo: ItemModel.TodoItem, completed: Boolean) {
        updateTodo.invoke(todo.copy(isCompleted = completed).toTodo())
    }

    fun onShowCompletedChanged(show: Boolean) {
        Log.d(TAG, "onShowCompletedChanged: $show")
        showCompleted = show
        _todos.value = mapEntriesByShowCompleted(todoItems)
    }

    private fun mapEntriesToTodoItems(entries: List<Todo>): List<ItemModel.TodoItem> {
        return entries.map {
            it.toTodoItem()
        }
    }

    private fun mapEntriesByShowCompleted(entries: List<ItemModel.TodoItem>): List<ItemModel> {
        val finalList = mutableListOf<ItemModel>()
        val actualTodos = mutableListOf<ItemModel>()
        val completedTodos = mutableListOf<ItemModel>()
        if (showCompleted) {
            entries.forEach {
                if (!it.isCompleted) actualTodos.add(it)
                else completedTodos.add(it)
            }
        } else {
            entries.forEach {
                if (!it.isCompleted) actualTodos.add(it)
            }
        }
        finalList.addAll(actualTodos)
        finalList.add(ItemModel.Divider(isCompletedDisplayed = showCompleted) as ItemModel)
        if (showCompleted) finalList.addAll(completedTodos)

        return finalList
    }

    override fun onCleared() {
        super.onCleared()
        compDisp.clear()
    }
}

