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
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.AlarmUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.todo.TodoUseCases
import com.example.todolistapp.feature_todo_list.domain.util.Constants.ACTION_OPEN_LIST
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_ALARM
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import com.google.gson.Gson
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmUseCases: AlarmUseCases

    @Inject
    lateinit var todoUseCases: TodoUseCases

    override fun onReceive(context: Context, intent: Intent?) {
        TodoListApp.component?.inject(this)

        val todoJson = intent?.getStringExtra(TODO_MODEL)
        val todo = Gson().fromJson(todoJson, Todo::class.java)

        todo?.id?.let { id ->
            alarmUseCases.removeAlarm.invoke(todo)
            todoUseCases.updateTodo.invoke(todo.copy(needShowReminder = true)).subscribe()

            val mainIntent = Intent(context, MainActivity::class.java).apply {
                setAction(ACTION_OPEN_LIST)
            }
            mainIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val mainPendingIntent = PendingIntent.getActivity(
                context,
                0,
                mainIntent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            )

            val completeIntent = Intent(context, AlarmNotificationReceiver::class.java).apply {
                putExtra(TODO_MODEL, todoJson)
            }
            val completePendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                completeIntent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, TodoListApp.ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(context.getString(R.string.reminder))
                .setContentText(todo.text.take(20))
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .addAction(0, context.getString(R.string.completed), completePendingIntent)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(id, notification)

        } ?: run {
            Log.d(TAG_ALARM, "AlarmReceiver.onReceive: todo.id is NULL")
        }
    }
}