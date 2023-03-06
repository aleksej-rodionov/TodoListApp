package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoListBinding
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import com.example.todolistapp.feature_todo_list.domain.util.Constants
import com.example.todolistapp.feature_todo_list.domain.util.Constants.TAG_DIALOG
import com.example.todolistapp.feature_todo_list.presentation.todo_editor.TodoEditorFragmentArgs
import com.google.gson.Gson
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

private const val TAG = "TodoListFragment"

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

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val todo = arguments?.getString(Constants.TODO_MODEL)
////        Log.d(Constants.TAG_DIALOG, "onCreateView: todo = $todo")
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodoListBinding.bind(view)

        val args by navArgs<TodoListFragmentArgs>()
        val todo = args.todoJson
//        Log.d(TAG_DIALOG, "onViewCreated: todoJson = ${todo ?: "NULL"}")

        initRecyclerView()
        initListeners()
    }

    override fun showTodos(todos: List<ItemModel>) {
        todoListAdapter?.setEntries(todos)
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

    fun showDialog(todo: String) {
        Log.d(TAG_DIALOG, "showDialog: todo = $todo")
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

    private fun showReminderDialog(todoJson: String) {// todo call from presenter
        Log.d(TAG_DIALOG, "showDialog: $todoJson")
        val todo = Gson().fromJson(todoJson, Todo::class.java)

        val builder = AlertDialog.Builder(requireContext()).apply {
            setMessage(todo.text.take(20))
            setPositiveButton(resources.getString(R.string.completed)) { dialog, which ->
                presenter.onCompletedChanged(todo, true)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}