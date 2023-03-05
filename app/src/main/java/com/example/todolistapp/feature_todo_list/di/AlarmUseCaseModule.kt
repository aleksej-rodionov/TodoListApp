package com.example.todolistapp.feature_todo_list.di

import android.content.Context
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.AlarmUseCases
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.alarm.SetAlarm
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AlarmUseCaseModule {

    @Provides
    @Singleton
    fun provideAlarmUseCases(context: Context): AlarmUseCases {
        return AlarmUseCases(
            setAlarm = SetAlarm(context),
            removeAlarm = RemoveAlarm(context),
            checkIfAlarmSet = CheckIfAlarmSet(context)
        )
    }
}