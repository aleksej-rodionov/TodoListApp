package com.example.todolistapp.feature_todo_list.domain.use_case

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_ID
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import com.example.todolistapp.feature_todo_list.presentation.alarm.AlarmReceiver
import javax.inject.Inject

class SetAlarm @Inject constructor(
    private val context: Context
) {

    operator fun invoke(todo: Todo) {

        todo.id?.let { id ->

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(TODO_MODEL, todo)
            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)

            val mainActivityIntent = Intent(context, MainActivity::class.java)
            val basicPendingIntent = PendingIntent.getActivity(context, id, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

            val clockInfo = AlarmManager.AlarmClockInfo(countAlarmTime(), basicPendingIntent)
            alarmManager.setAlarmClock(clockInfo, pendingIntent)
        } ?: throw IllegalArgumentException()
    }

    private fun countAlarmTime(): Long {
        return System.currentTimeMillis() + 10000 //todo change to 600000
    }
}