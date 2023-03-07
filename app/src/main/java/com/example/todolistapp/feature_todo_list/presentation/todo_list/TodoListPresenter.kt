package com.example.todolistapp.feature_todo_list.presentation.todo_list

import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.AlarmUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.TodoUseCases
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.concurrent.TimeUnit

@InjectViewState
class TodoListPresenter(
    private val alarmUseCases: AlarmUseCases,
    private val todoUseCases: TodoUseCases
): MvpPresenter<TodoListView>() {

    private var compDisp: CompositeDisposable? = null

    private var showCompleted = false
    private val todoItems = mutableListOf<ItemModel.TodoItem>()

    private var todosToRemind = mutableListOf<Todo>()

    init {
        observeAllTodos()
    }

    private fun observeAllTodos() {
        viewState.showHideLoader(true)
        val output = todoUseCases.getAllTodos.invoke()
        output
            .delay(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showHideLoader(false)
                viewState.showHideNoTodosText(it.isEmpty())
                todoItems.clear()
                if (it.isNotEmpty()) {
                    todoItems.addAll(mapEntriesToTodoItems(it))
                    viewState.showTodos(mapEntriesByShowCompleted(todoItems))
                } else {
                    viewState.showTodos(todoItems)
                }
            }, {
                it.printStackTrace()
            }).apply { compDisp?.add(this) }
    }

    fun onCompletedChanged(todo: Todo, completed: Boolean) {
        var updatedTodo = todo.copy(isCompleted = completed)
        if (completed) {
            updatedTodo = updatedTodo.copy(needShowReminder = false)
            alarmUseCases.removeAlarm.invoke(todo)
        }
        todoUseCases.updateTodo.invoke(updatedTodo).subscribe()
    }

    fun onShowCompletedChanged(show: Boolean) {
        showCompleted = show
        viewState.showTodos(mapEntriesByShowCompleted(todoItems))
    }

    fun onAddTodoClick() {
        viewState.navigateAddTodo()
    }

    fun onEditTodoClick(todo: Todo) {
        viewState.navigateEditTodo(todo)
    }

    fun clearTodoFromReminderList(todo: Todo) {
        todosToRemind.removeAll {
            it.id == todo.id
        }
    }

    fun showRemindersFromReminderList() {
        todosToRemind.forEach {
            viewState.showReminderDialog(it)
        }
    }

    private fun mapEntriesToTodoItems(entries: List<Todo>): List<ItemModel.TodoItem> {
        return entries.map {
            if (it.needShowReminder) {

                todoUseCases.updateTodo.invoke(it.copy(needShowReminder = false)).subscribe()
                viewState.showReminderDialog(it)
                todosToRemind.add(it)
            }

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

    fun showHideNoTodosText(show: Boolean)

    fun showHideLoader(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showReminderDialog(todo: Todo)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateAddTodo()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateEditTodo(todo: Todo?)
}