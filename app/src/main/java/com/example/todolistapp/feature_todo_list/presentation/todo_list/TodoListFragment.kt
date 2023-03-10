package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoListBinding
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TodoListFragment : MvpAppCompatFragment(R.layout.fragment_todo_list), TodoListView {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    @Inject
    @InjectPresenter
    lateinit var presenter: TodoListPresenter

    @ProvidePresenter
    fun provideTodoListPresenter(): TodoListPresenter = presenter

    private var todoListAdapter: TodoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        TodoListApp.component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodoListBinding.bind(view)

        initRecyclerView()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.showRemindersFromReminderList()
    }

    override fun showTodos(todos: List<ItemModel>) {
        todoListAdapter?.setEntries(todos)
    }

    override fun showHideNoTodosText(show: Boolean) {
        binding.tvNoTodos.visibility = if (show) View.VISIBLE
        else View.GONE
    }

    override fun showHideLoader(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE
        else View.GONE
    }

    override fun navigateAddTodo() {
        val action =
            TodoListFragmentDirections.actionTodoListFragmentToTodoEditorFragment(null)
        findNavController().navigate(action)
    }

    override fun navigateEditTodo(todo: Todo?) {
        val action =
            TodoListFragmentDirections.actionTodoListFragmentToTodoEditorFragment(todo)
        findNavController().navigate(action)
    }

    private fun initRecyclerView() {
        todoListAdapter = TodoListAdapter(
            onTodoClick = {
                presenter.onEditTodoClick(it.toTodo())
            },
            onCompletedClick = { todo, completed ->
                presenter.onCompletedChanged(todo.toTodo(), completed)
            },
            onShowCompletedChanged = {
                presenter.onShowCompletedChanged(it)
            }
        )

        binding.rvTodos.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initListeners() {
        binding.apply {
            fab.setOnClickListener {
                presenter.onAddTodoClick()
            }
        }
    }

    override fun showReminderDialog(todo: Todo) {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle(resources.getString(R.string.reminder))
            setMessage(todo.text.take(20))
            setPositiveButton(resources.getString(R.string.completed)) { dialog, which ->
                presenter.onCompletedChanged(todo, true)
                presenter.clearTodoFromReminderList(todo)
            }
        }
        val dialog = builder.create().apply {
            setCanceledOnTouchOutside(true)
            setOnCancelListener {
                presenter.clearTodoFromReminderList(todo)
            }
        }
        dialog.show()
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.secondary))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}