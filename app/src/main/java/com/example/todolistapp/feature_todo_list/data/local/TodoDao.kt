package com.example.todolistapp.feature_todo_list.data.local

import androidx.room.*
import com.example.todolistapp.feature_todo_list.data.local.entity.TodoEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo: TodoEntity)

    @Update
    fun updateRodo(todo: TodoEntity)

    @Delete
    fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todoentity WHERE id = :id")
    fun getTodoById(id: Int): Single<TodoEntity>

    @Query("SELECT * FROM todoentity")
    fun getAllTodos(): Flowable<List<TodoEntity>>
}