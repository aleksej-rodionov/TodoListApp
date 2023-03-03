package com.example.todolistapp.feature_todo_list.domain.use_case

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.todolistapp.feature_todo_list.presentation.alarm.AlarmReceiver

@SuppressLint("UnspecifiedImmutableFlag")
class CheckIfAlarmSet(
    private val context: Context
) {

    operator fun invoke(todoId: Int): Boolean {

        val intent = Intent(context, AlarmReceiver::class.java)
        val isAlarmSet: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                todoId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            )
            PendingIntent.getBroadcast(
                context,
                todoId,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            ) != null
        } else {
            PendingIntent.getBroadcast(
                context,
                todoId,
                intent,
                PendingIntent.FLAG_NO_CREATE
            )
            PendingIntent.getBroadcast(
                context,
                todoId,
                intent,
                PendingIntent.FLAG_NO_CREATE
            ) != null
        }

        return isAlarmSet
    }
}