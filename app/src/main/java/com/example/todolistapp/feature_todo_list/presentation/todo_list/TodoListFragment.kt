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
import com.example.todolistapp.feature_todo_list.domain.model.Todo
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
//                viewModel.onTodoClick(it)
                navigateEditTodo(it)
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

//        viewModel.uiEffect.observe(viewLifecycleOwner) {
//            when (it.peekContent()) {
//                is TodoListViewModel.UiEffect.OnNavigateAddTodo -> {
//                   navigateAddTodo()
//                }
//                is TodoListViewModel.UiEffect.OnNavigateEditTodo -> {
//                    navigateEditTodo((it.peekContent() as? TodoListViewModel.UiEffect.OnNavigateEditTodo)?.todo)
//                }
//            }
//        }
    }

    private fun initListeners() {
        binding.apply {
            fab.setOnClickListener {
//                viewModel.onAddTodoClick()
                navigateAddTodo()
            }
        }
    }

    private fun navigateAddTodo() {
        val action =
            TodoListFragmentDirections.actionTodoListFragmentToTodoEditorFragment(null)
        findNavController().navigate(action)
    }

    private fun navigateEditTodo(todo: Todo?) {
        val action =
            TodoListFragmentDirections.actionTodoListFragmentToTodoEditorFragment(todo)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}