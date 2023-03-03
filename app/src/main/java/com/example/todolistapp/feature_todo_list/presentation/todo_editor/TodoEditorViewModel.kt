package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.SetAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.UpdateTodo
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import javax.inject.Inject

private const val TAG = "TodoEditorViewModel"

class TodoEditorViewModel @Inject constructor(
    private val reposiroty: TodoRepository, //todo useCase/interactor
    private val setAlarm: SetAlarm,
    private val removeAlarm: RemoveAlarm,
    private val checkIfAlarmSet: CheckIfAlarmSet,
    private val updateTodo: UpdateTodo
) : ViewModel() {

    private var pendingSaveTodoToSetAlarm = false

    var todoId: Int? = null
    private var todoCompleted: Boolean = false
    var todoText: String = ""

    private val _isAlarmSet = MutableLiveData<Boolean>(false)
    val isAlarmSet: LiveData<Boolean> = _isAlarmSet

    fun initAlarmState() {
        _isAlarmSet.value = checkIfAlarmSet()
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

//            if (pendingSaveTodoToSetAlarm) {
//                val
//            }

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
    }

    fun onAlarmClick() {
        if (checkIfAlarmSet()) {
            try {
                removeAlarm()
                _isAlarmSet.value = false
            } catch (e: Exception) {
                Log.d(TAG_ALARM, "onAlarmClick: exception = ${e.message}")
                e.printStackTrace()
            }
        } else {
            try {
                setAlarm()
                _isAlarmSet.value = true
            } catch (e: Exception) {
                Log.d(TAG_ALARM, "onAlarmClick: exception = ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun testCheck() {
        val alarmSet = checkIfAlarmSet()
//        Log.d(TAG_ALARM, "testCheck: $alarmSet")
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
}
