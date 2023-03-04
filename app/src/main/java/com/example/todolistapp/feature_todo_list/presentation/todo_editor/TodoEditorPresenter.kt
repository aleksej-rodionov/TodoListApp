package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import com.example.todolistapp.feature_todo_list.domain.repository.TodoRepository
import com.example.todolistapp.feature_todo_list.domain.use_case.CheckIfAlarmSet
import com.example.todolistapp.feature_todo_list.domain.use_case.RemoveAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.SetAlarm
import com.example.todolistapp.feature_todo_list.domain.use_case.UpdateTodo
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

private const val TAG = "TodoEditorPresenter"

@InjectViewState
class TodoEditorPresenter(
    private val reposiroty: TodoRepository, //todo useCase/interactor
    private val setAlarm: SetAlarm,
    private val removeAlarm: RemoveAlarm,
    private val checkIfAlarmSet: CheckIfAlarmSet,
    private val updateTodo: UpdateTodo
): MvpPresenter<TodoEditorView>() {

    private var compDisp: CompositeDisposable? = null



    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        compDisp = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        compDisp?.clear()
    }
}



@StateStrategyType(AddToEndStrategy::class)
interface TodoEditorView: MvpView {

    fun showTodoText(text: String)

    fun showIsAlarmSet(set: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateUp()
}