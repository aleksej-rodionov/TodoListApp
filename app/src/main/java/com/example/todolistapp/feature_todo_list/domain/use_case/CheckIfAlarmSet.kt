package com.example.todolistapp.feature_todo_list.domain.use_case

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import com.example.todolistapp.feature_todo_list.presentation.alarm.AlarmReceiver

@SuppressLint("UnspecifiedImmutableFlag")
class CheckIfAlarmSet(
    private val context: Context
) {

    operator fun invoke(todoId: Int): Boolean {

        val intent = Intent(context, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            todoId,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            } else PendingIntent.FLAG_NO_CREATE
        )
        val isAlarmSet = PendingIntent.getBroadcast(
            context,
            todoId,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
            } else PendingIntent.FLAG_NO_CREATE
        ) != null

        Log.d(TAG_ALARM, "CheckIfAlarmSet.invoke: CALLED for id = $todoId, result = $isAlarmSet")
        return isAlarmSet
    }
}