package com.example.todolistapp.feature_todo_list.domain.use_case

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import com.example.todolistapp.feature_todo_list.presentation.alarm.AlarmReceiver

class SetAlarm(
    private val context: Context
) {

    operator fun invoke(todo: Todo) {

        todo.id?.let { id ->
            Log.d(TAG_ALARM, "SetAlarm.invoke: CALLED for id = $id")

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(TODO_MODEL, todo)
//            intent.putExtra("pizda", 999)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE
                else 0
            )

            val mainActivityIntent = Intent(context, MainActivity::class.java)
            val basicPendingIntent = PendingIntent.getActivity(
                context,
                id,
                mainActivityIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE
                else 0
            )

            val clockInfo = AlarmManager.AlarmClockInfo(countAlarmTime(), basicPendingIntent)
            alarmManager.setAlarmClock(clockInfo, pendingIntent)
        } ?: run {
            Log.d(TAG_ALARM, "SetAlarm.invoke: exception")
            throw IllegalArgumentException()
        }
    }

    private fun countAlarmTime(): Long {
        return System.currentTimeMillis() + 10000 //todo change to 600000
    }
}