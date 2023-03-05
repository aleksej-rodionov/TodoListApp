package com.example.todolistapp.feature_todo_list.domain.use_case.alarm

data class AlarmUseCases(
    val setAlarm: SetAlarm,
    val removeAlarm: RemoveAlarm,
    val checkIfAlarmSet: CheckIfAlarmSet
)