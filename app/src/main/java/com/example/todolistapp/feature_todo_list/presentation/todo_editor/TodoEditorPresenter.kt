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

private const val TAG = "TodoEditorPresenter"

@InjectViewState
class TodoEditorPresenter(
    private val alarmUseCases: AlarmUseCases,
    private val todoUseCases: TodoUseCases
): MvpPresenter<TodoEditorView>() {

    private var compDisp: CompositeDisposable? = null

    private var pendingSaveTodoToSetAlarm = false

    var todoText: String = ""
    private var todoCompleted: Boolean = false
    var todoId: Int? = null
    private var needShowReminder: Boolean = false

    fun initAlarmState() {
        viewState.showIsAlarmSet(checkIfAlarmSet())
    }

    fun setTodo(todo: Todo?) {
        Log.d(TAG, "setTodo: called")
        todoId = todo?.id
        todoCompleted = todo?.isCompleted ?: false
        needShowReminder = todo?.needShowReminder ?: false
        updateText(todo?.text)
    }

    fun updateText(text: String?) {
        text?.let { todoText = it }
    }

    fun onSaveClick() {
        if (todoId == null) {
            val newTodo = Todo(
                text = todoText,
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
            val updatedTodo = Todo(
                text = todoText,
                isCompleted = todoCompleted,
                needShowReminder = needShowReminder,
                id = todoId
            )
            todoUseCases.updateTodo.invoke(updatedTodo)
        }
    }

    fun deleteTodo() {
        val todo = Todo(
            text = todoText,
            isCompleted = todoCompleted,
            needShowReminder = needShowReminder,
            id = todoId
        )
        todoUseCases.deleteTodo.invoke(todo)
        viewState.navigateUp()
    }

    fun onAlarmClick() {

        if (todoId == null) {
            pendingSaveTodoToSetAlarm = !pendingSaveTodoToSetAlarm
            viewState.showIsAlarmSet(pendingSaveTodoToSetAlarm)
        } else {
            if (checkIfAlarmSet()) {
                try {
                    removeAlarm()
                    viewState.showIsAlarmSet(false)
                } catch (e: Exception) {
                    Log.d(TAG_ALARM, "onAlarmClick: exception = ${e.message}")
                    e.printStackTrace()
                }
            } else {
                try {
                    setAlarm()
                    viewState.showIsAlarmSet(true)
                } catch (e: Exception) {
                    Log.d(TAG_ALARM, "onAlarmClick: exception = ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    fun testCheck() {
        val alarmSet = checkIfAlarmSet()
        Log.d(TAG_ALARM, "testCheck: $alarmSet")
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
        todoId?.let {
            alarmUseCases.setAlarm.invoke(Todo(todoText, todoCompleted, needShowReminder, it))
        } ?: run {
            pendingSaveTodoToSetAlarm = true
        }
    }

    private fun removeAlarm() {
        alarmUseCases.removeAlarm.invoke(Todo(todoText, todoCompleted, needShowReminder, todoId))
    }

    private fun checkIfAlarmSet(): Boolean {
        return todoId?.let {
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
interface TodoEditorView: MvpView {

    fun showIsAlarmSet(set: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateUp()
}