package com.example.todolistapp.feature_todo_list.presentation.alarm

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.MainActivity

private const val TAG = "AlarmReceiver"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        TodoListApp.component?.inject(this)

        val todo = intent?.getParcelableExtra(TODO_MODEL) as? Todo

        todo?.id?.let { id ->

            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val mainPendingIntent = PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            )

            val completeIntent = Intent(context, AlarmNotificationReceiver::class.java).apply {
                putExtra(TODO_MODEL, todo)
            }
            val completePendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                completeIntent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, TodoListApp.ALARM_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //todo remove?
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Уведомление") //todo resources
                .setContentText(todo.text.take(20))
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .addAction(0, "Выполнено", completePendingIntent) //todo resources
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(id, notification)
        } ?: run {
            Log.d(TAG, "onReceive: todo.id is NULL")
        }
    }
}