package com.example.todolistapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.todolistapp.feature_todo_list.di.DaggerSingletonComponent
import com.example.todolistapp.feature_todo_list.di.SingletonComponent

class TodoListApp: Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerSingletonComponent.builder().application(this).build()
        component?.inject(this)

        createAlarmNotificationChannel()
    }

    private fun createAlarmNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmNotificationChannel = NotificationChannel(
                ALARM_CHANNEL_ID,
                "Alarm Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(alarmNotificationChannel)
        }
    }

    companion object {
        var component: SingletonComponent? = null

        const val ALARM_CHANNEL_ID = "alarmNotificationChannel"
    }
}