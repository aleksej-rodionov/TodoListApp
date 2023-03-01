package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoEditorBinding
import com.example.todolistapp.feature_todo_list.di.ViewModelFactory
import com.example.todolistapp.feature_todo_list.presentation.todo_list.TodoListViewModel
import javax.inject.Inject

class TodoEditorFragment : Fragment(R.layout.fragment_todo_editor) {

    private var _binding: FragmentTodoEditorBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by viewModels<TodoEditorViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        TodoListApp.component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodoEditorBinding.bind(view)

        val args by navArgs<TodoEditorFragmentArgs>()
        viewModel.setTodo(args.todo)

        initObservers()
    }

    private fun initObservers() {
        viewModel.todoText.observe(viewLifecycleOwner) {
            binding.etText.setText(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}