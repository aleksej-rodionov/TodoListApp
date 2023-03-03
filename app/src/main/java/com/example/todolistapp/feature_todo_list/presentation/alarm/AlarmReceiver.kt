package com.example.todolistapp.feature_todo_list.presentation.alarm

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.repository.TodoReposiroty
import com.example.todolistapp.feature_todo_list.domain.util.Constants.DEFAULT_INT
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_ID
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TODO_MODEL
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    private lateinit var reposiroty: TodoReposiroty

    override fun onReceive(context: Context?, intent: Intent?) {

        val todo = intent?.getIntExtra(TODO_MODEL, DEFAULT_INT) as? Todo

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

            //notifBuilder
        }
    }
}