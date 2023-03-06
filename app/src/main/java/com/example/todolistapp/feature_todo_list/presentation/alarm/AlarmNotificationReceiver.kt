package com.example.todolistapp.feature_todo_list.presentation.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.TodoUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.UpdateTodo
import com.example.todolistapp.feature_todo_list.domain.util.Constants
import com.google.gson.Gson
import javax.inject.Inject

class AlarmNotificationReceiver: BroadcastReceiver() {

    @Inject
    lateinit var todoUseCases: TodoUseCases

    override fun onReceive(context: Context, intent: Intent?) {
        TodoListApp.component?.inject(this)

        val todoString = intent?.getStringExtra(Constants.TODO_MODEL)
        val todo = Gson().fromJson(todoString, Todo::class.java)

        todo?.id?.let { id ->

            todoUseCases.updateTodo.invoke(todo.copy(isCompleted = true, needShowReminder = false))
            NotificationManagerCompat.from(context).cancel(null, id)
        }
    }
}