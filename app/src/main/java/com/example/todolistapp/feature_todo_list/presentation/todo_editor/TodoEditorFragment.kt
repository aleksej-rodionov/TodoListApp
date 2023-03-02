package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoEditorBinding
import com.example.todolistapp.feature_todo_list.di.ViewModelFactory
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import kotlinx.android.synthetic.main.fragment_todo_editor.*
import javax.inject.Inject

private const val TAG = "TodoEditorFragment"

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

        setHasOptionsMenu(true)

        val args by navArgs<TodoEditorFragmentArgs>()
        val todo = args.todo
        viewModel.setTodo(todo)
        (activity as MainActivity).supportActionBar?.title = if (todo == null) "Новая заметка"
        else "Заметка" //todo resource



        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        etText.setText(viewModel.todoText.value)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.todoId == null) {
            inflater.inflate(R.menu.menu_add_todo, menu)
        } else {
            inflater.inflate(R.menu.menu_edit_todo, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteTodo()
                navigateBack()
                true
            }
         else ->   super.onOptionsItemSelected(item)
        }
    }

    private fun initObservers() {
//        viewModel.todoText.observe(viewLifecycleOwner) {
//            Log.d(TAG, "initObservers: text = $it")
//            binding.etText.setText(it)
//        }

//        viewModel.uiEffect.observe(viewLifecycleOwner) {
//            when (it.peekContent()) {
//                is TodoEditorViewModel.UiEffect.NavigateBack -> {
//                    Log.d(TAG, "initObservers: NavigateBack Effect received")
//                    navigateBack()
//                }
//            }
//        }
    }

    private fun initListeners() {
        binding.apply {
            etText.addTextChangedListener {
                viewModel.updateText(it.toString())
            }

            fab.setOnClickListener {
                viewModel.onSaveClick()
                navigateBack()
            }
        }
    }

    private fun navigateBack() {
        binding.etText.clearFocus()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}