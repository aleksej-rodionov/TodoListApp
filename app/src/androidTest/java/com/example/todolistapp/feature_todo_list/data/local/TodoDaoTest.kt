package com.example.todolistapp.feature_todo_list.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.todolistapp.feature_todo_list.data.local.mapper.toTodoEntity
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.google.common.truth.Truth.assertThat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TodoDaoTest {

    var compDisp: CompositeDisposable? = null

    private lateinit var database: TodoDatabase
    private lateinit var dao: TodoDao

    @Before
    fun setUp() {
        compDisp = CompositeDisposable()

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao()
    }

    @After
    fun tearDown() {
        compDisp?.clear()

        database.close()
    }

    @Test
    fun get_todo_by_id_works_properly() {
        val todo = Todo(text = "testTodo")
        dao.insertTodo(todo.toTodoEntity()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dao.getTodoById(it.toInt()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        assertThat(it.text).isEqualTo(todo.text)
                    }, {
                        it.printStackTrace()
                    }).apply { compDisp?.add(this) }
            }, {
                it.printStackTrace()
            }).apply { compDisp?.add(this) }
    }

    @Test
    fun update_todo_works_properly() {
        val todo = Todo(text = "testTodo")
        dao.insertTodo(todo.toTodoEntity()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ id ->
                dao.updateTodo(todo.copy(text = "anotherTestTodo", id = id.toInt()).toTodoEntity()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        dao.getTodoById(id.toInt()).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                assertThat(it.text).isEqualTo("anotherTestTodo")
                            }, {
                                it.printStackTrace()
                            }).apply { compDisp?.add(this) }
                    }, {
                        it.printStackTrace()
                    }).apply { compDisp?.add(this) }
            }, {
                it.printStackTrace()
            }).apply { compDisp?.add(this) }
    }
}