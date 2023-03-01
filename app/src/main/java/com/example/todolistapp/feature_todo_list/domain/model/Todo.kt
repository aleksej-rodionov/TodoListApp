package com.example.todolistapp.feature_todo_list.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Todo(
    val text: String,
    val isCompleted: Boolean,
    val id: Int? = null
): Parcelable
