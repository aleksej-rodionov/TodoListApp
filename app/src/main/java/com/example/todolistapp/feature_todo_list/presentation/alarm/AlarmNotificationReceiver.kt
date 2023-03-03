package com.example.todolistapp.feature_todo_list.presentation.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.use_case.UpdateTodo
import com.example.todolistapp.feature_todo_list.domain.util.Constants
import javax.inject.Inject

class AlarmNotificationReceiver: BroadcastReceiver() {

    @Inject
    lateinit var updateTodo: UpdateTodo

    override fun onReceive(context: Context, intent: Intent?) {

        TodoListApp.component?.inject(this)

        val todo = intent?.getParcelableExtra(Constants.TODO_MODEL) as? Todo

        todo?.id?.let { id ->

            updateTodo.invoke(todo)
            NotificationManagerCompat.from(context).cancel(null, id)
        }
    }
}