package com.example.todolistapp.feature_todo_list.domain.use_case

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.util.Constants
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_ID
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.alarm.AlarmReceiver
import javax.inject.Inject

class RemoveAlarm(
    private val context: Context
) {

    operator fun invoke(todo: Todo) {

        todo.id?.let { id ->
            Log.d(TAG_ALARM, "RemoveAlarm.invoke: CALLED for id = $id")

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(TODO_MODEL, todo)
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
            Log.d(Constants.TAG_ALARM, "RemoveAlarm.invoke: exception")
            throw IllegalArgumentException()
        }
    }
}