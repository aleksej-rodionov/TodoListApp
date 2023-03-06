package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.AlarmUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.SetAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.TodoUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.UpdateTodo
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@InjectViewState
class TodoEditorPresenter(
    private val alarmUseCases: AlarmUseCases,
    private val todoUseCases: TodoUseCases
) : MvpPresenter<TodoEditorView>() {

    private var compDisp: CompositeDisposable? = null

    private var pendingSaveTodoToSetAlarm = false

    private var _todoToEdit: Todo = Todo()
    val todoToEdit get() = _todoToEdit

    fun initAlarmState() {
        viewState.showIsAlarmSet(checkIfAlarmSet())
    }

    fun setTodo(todo: Todo?) {
        todo?.let {
            _todoToEdit = it
        }
    }

    fun updateText(text: String?) {
        text?.let { _todoToEdit = todoToEdit.copy(text = it) }
    }

    fun onSaveClick() {
        if (todoToEdit.id == null) {
            val newTodo = Todo(
                text = todoToEdit.text,
                isCompleted = false,
                needShowReminder = false
            )
            todoUseCases.insertTodo.invoke(newTodo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (pendingSaveTodoToSetAlarm) {
                        val id = it.toInt()
                        fetchNewTodoAndSetAlarmForIt(id)
                    } else {
                        viewState.navigateUp()
                    }
                }, {
                    it.printStackTrace()
                }).apply { compDisp?.add(this) }
        } else {
            val updatedTodo = todoToEdit.copy()
            todoUseCases.updateTodo.invoke(updatedTodo).subscribe()
            viewState.navigateUp()
        }
    }

    fun deleteTodo() {
        val todo = todoToEdit.copy()
        todoUseCases.deleteTodo.invoke(todo).subscribe()
        viewState.navigateUp()
    }

    fun onAlarmClick() {
        if (todoToEdit.id == null) {
            pendingSaveTodoToSetAlarm = !pendingSaveTodoToSetAlarm
            viewState.showIsAlarmSet(pendingSaveTodoToSetAlarm)
        } else {
            if (checkIfAlarmSet()) {
                removeAlarm()
                viewState.showIsAlarmSet(false)
            } else {
                setAlarm()
                viewState.showIsAlarmSet(true)
            }
        }
    }

    private fun fetchNewTodoAndSetAlarmForIt(id: Int) {
        todoUseCases.getTodoById.invoke(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                alarmUseCases.setAlarm.invoke(it)
                viewState.navigateUp()
            }, {
                it.printStackTrace()
            }).apply { compDisp?.add(this) }
    }

    private fun setAlarm() {
        todoToEdit.id?.let {
            alarmUseCases.setAlarm.invoke(todoToEdit.copy())
        } ?: run {
            pendingSaveTodoToSetAlarm = true
        }
    }

    private fun removeAlarm() {
        alarmUseCases.removeAlarm.invoke(todoToEdit.copy())
    }

    private fun checkIfAlarmSet(): Boolean {
        return todoToEdit.id?.let {
            alarmUseCases.checkIfAlarmSet.invoke(it)
        } ?: false
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
interface TodoEditorView : MvpView {

    fun showIsAlarmSet(set: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateUp()
}