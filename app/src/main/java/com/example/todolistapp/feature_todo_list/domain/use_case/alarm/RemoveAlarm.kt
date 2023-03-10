package com.example.todolistapp.feature_todo_list.domain.use_case.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.util.Constants
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.alarm.AlarmReceiver
import com.google.gson.Gson

class RemoveAlarm(
    private val context: Context
) {

    operator fun invoke(todo: Todo) {

        todo.id?.let { id ->
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val todoString = Gson().toJson(todo)
            intent.putExtra(TODO_MODEL, todoString)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE
                else 0
            )
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        } ?: run {
            Log.d(TAG_ALARM, "RemoveAlarm.invoke: exception")
            throw IllegalArgumentException()
        }
    }
}