package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.AlarmUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.TodoUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.UpdateTodo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

private const val TAG = "TodoListPresenter"

@InjectViewState
class TodoListPresenter(
    private val alarmUseCases: AlarmUseCases,
    private val todoUseCases: TodoUseCases
): MvpPresenter<TodoListView>() {

    private var compDisp: CompositeDisposable? = null

    private var showCompleted = false
    private val todoItems = mutableListOf<ItemModel.TodoItem>()

    init {
        observeAllTodos()
    }

    private fun observeAllTodos() {
        val output = todoUseCases.getAllTodos.invoke()
        output.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                todoItems.clear()
                todoItems.addAll(mapEntriesToTodoItems(it))
                viewState.showTodos(mapEntriesByShowCompleted(todoItems))
            }, {
                Log.d("TAG_TODO_LIST", "observeAllTodos: error = ${it.message}")
                it.printStackTrace()
            }).apply { compDisp?.add(this) }
    }

    fun onCompletedChanged(todo: ItemModel.TodoItem, completed: Boolean) {
        todoUseCases.updateTodo.invoke(todo.copy(isCompleted = completed).toTodo())
    }

    fun onShowCompletedChanged(show: Boolean) {
        Log.d(TAG, "onShowCompletedChanged: $show")
        showCompleted = show
        viewState.showTodos(mapEntriesByShowCompleted(todoItems))
    }

    fun onAddTodoClick() {
        viewState.navigateAddTodo()
    }

    fun onEditTodoClick(todo: Todo) {
        viewState.navigateEditTodo(todo)
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        compDisp = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        compDisp?.clear()
    }
}

@StateStrategyType(AddToEndStrategy::class)
interface TodoListView: MvpView {

    fun showTodos(todos: List<ItemModel>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateAddTodo()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateEditTodo(todo: Todo?)
}