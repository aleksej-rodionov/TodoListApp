package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.SetAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.UpdateTodo
import com.example.todolistapp.feature_todo_list.domain.util.Constants
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
    private val reposiroty: TodoRepository, //todo useCase/interactor
    private val setAlarm: SetAlarm,
    private val removeAlarm: RemoveAlarm,
    private val checkIfAlarmSet: CheckIfAlarmSet,
    private val updateTodo: UpdateTodo
): MvpPresenter<TodoEditorView>() {

    private var compDisp: CompositeDisposable? = null

    private var pendingSaveTodoToSetAlarm = false

    var todoId: Int? = null
    private var todoCompleted: Boolean = false
    var todoText: String = ""

    fun initAlarmState() {
        viewState.showIsAlarmSet(checkIfAlarmSet())
    }

    fun setTodo(todo: Todo?) {
        Log.d(TAG, "setTodo: called")
        todoId = todo?.id
        todoCompleted = todo?.isCompleted ?: false
        updateText(todo?.text)
    }

    fun updateText(text: String?) {
        text?.let { todoText = it }
    }

    fun onSaveClick() {
        if (todoId == null) {
            val newTodo = Todo(
                text = todoText,
                isCompleted = false
            )
            reposiroty.insertTodo(newTodo)
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
                id = todoId
            )
            updateTodo.invoke(updatedTodo)
        }
    }

    fun deleteTodo() {
        val todo = Todo(
            text = todoText,
            isCompleted = todoCompleted,
            id = todoId
        )
        reposiroty.deleteTodo(todo)
        viewState.navigateUp()
    }

    fun onAlarmClick() {
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

    fun testCheck() {
        val alarmSet = checkIfAlarmSet()
        Log.d(TAG_ALARM, "testCheck: $alarmSet")
    }

    private fun fetchNewTodoAndSetAlarmForIt(id: Int) {
        reposiroty.getTodoById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setAlarm(it)
                viewState.navigateUp()
            }, {
                it.printStackTrace()
            }).apply { compDisp?.add(this) }
    }

    private fun setAlarm() {
        todoId?.let {
            setAlarm.invoke(Todo(todoText, todoCompleted, it))
        } ?: run {
            pendingSaveTodoToSetAlarm = true
        }
    }

    private fun removeAlarm() {
        removeAlarm.invoke(Todo(todoText, todoCompleted, todoId))
    }

    private fun checkIfAlarmSet(): Boolean {
        return todoId?.let {
            checkIfAlarmSet.invoke(it)
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