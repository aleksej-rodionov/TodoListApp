package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoListBinding
import com.example.todolistapp.feature_todo_list.di.ViewModelFactory
import com.example.todolistapp.feature_todo_list.domain.model.Todo
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

private const val TAG = "TodoListFragment"

class TodoListFragment : MvpAppCompatFragment(R.layout.fragment_todo_list), TodoListView {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

//    @Inject
//    lateinit var factory: ViewModelFactory
//    private val viewModel by viewModels<TodoListViewModel> { factory }

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
//        initObservers()
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

    private fun initRecyclerView() {
        todoListAdapter = TodoListAdapter(
            onTodoClick = {
//                navigateEditTodo(it.toTodo()) // todo call from presenter uiEffect
                presenter.onEditTodoClick(it.toTodo())
            },
            onCompletedClick = { todo, completed ->
//                viewModel.onCompletedChanged(todo, completed)
                presenter.onCompletedChanged(todo, completed)
            },
            onShowCompletedChanged = {
//                viewModel.onShowCompletedChanged(it)
                presenter.onShowCompletedChanged(it)
            }
        )

        binding.rvTodos.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

//    private fun initObservers() {
//        viewModel.todos.observe(viewLifecycleOwner) {
//            Log.d(TAG, "initObservers: list = $it")
//            todoListAdapter?.setEntries(it)
//        }
//    }

    private fun initListeners() {
        binding.apply {
            fab.setOnClickListener {
//                navigateAddTodo() // todo call from presenter uiEffect
                presenter.onAddTodoClick()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}