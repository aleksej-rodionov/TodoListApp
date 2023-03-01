package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoListBinding
import com.example.todolistapp.feature_todo_list.di.ViewModelFactory
import javax.inject.Inject

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<TodoListViewModel> { factory }

    private var todoListAdapter: TodoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        TodoListApp.component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodoListBinding.bind(view)

        initRecyclerView()
        initObservers()
        initListeners()
    }

    private fun initRecyclerView() {
        todoListAdapter = TodoListAdapter(
            requireContext(),
            onTodoClick = {
                viewModel.onTodoClick(it)
            },
            onCompletedClick = { todo, completed ->
                viewModel.onCompletedChanged(todo, completed)
            }
        )

        binding.rvTodos.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObservers() {
        viewModel.todos.observe(viewLifecycleOwner) {
            todoListAdapter?.submitList(it)
        }

        viewModel.uiEffect.observe(viewLifecycleOwner) {
            when (it.peekContent()) {
                is UiEffect.OnNavigateAddTodo -> {
                    val action =
                        TodoListFragmentDirections.actionTodoListFragmentToTodoEditorFragment(null)
                    findNavController().navigate(action)
                }
                is UiEffect.OnNavigateEditTodo -> {
                    val action =
                        TodoListFragmentDirections.actionTodoListFragmentToTodoEditorFragment(
                            (it.peekContent() as? UiEffect.OnNavigateEditTodo)?.todo
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            fab.setOnClickListener {
                viewModel.onAddTodoClick()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}