package com.example.todolistapp.feature_todo_list.presentation.todo_editor

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolistapp.R
import com.example.todolistapp.TodoListApp
import com.example.todolistapp.databinding.FragmentTodoEditorBinding
import com.example.todolistapp.feature_todo_list.presentation.MainActivity
import kotlinx.android.synthetic.main.fragment_todo_editor.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

private const val TAG = "TodoEditorFragment"

class TodoEditorFragment : MvpAppCompatFragment(R.layout.fragment_todo_editor), TodoEditorView {

    private var _binding: FragmentTodoEditorBinding? = null
    private val binding get() = _binding!!

    private var menu: Menu? = null

    @Inject
    @InjectPresenter
    lateinit var presenter: TodoEditorPresenter

    @ProvidePresenter
    fun provideTodoEditorPresenter(): TodoEditorPresenter = presenter

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
        presenter.setTodo(todo)
        (activity as MainActivity).supportActionBar?.title = if (todo == null) requireContext().getString(R.string.new_todo)
        else requireContext().getString(R.string.todo)

        initListeners()
    }

    override fun onResume() {
        super.onResume()
        etText.setText(presenter.todoText)
        presenter.initAlarmState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_todo, menu)

        this.menu = menu
        if (presenter.todoId == null) {
            menu.findItem(R.id.action_delete).setVisible(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                presenter.deleteTodo()
                true
            }
            R.id.action_reminder -> {
                presenter.onAlarmClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showIsAlarmSet(set: Boolean) {
        menu?.findItem(R.id.action_reminder)?.setIcon(
            if (set) R.drawable.ic_baseline_notifications_24
            else R.drawable.ic_baseline_notifications_off_24
        )
    }

    override fun navigateUp() {
        binding.etText.clearFocus()
        findNavController().popBackStack()
    }

    private fun initListeners() {
        binding.apply {
            etText.addTextChangedListener {
                presenter.updateText(it.toString())
            }

            fab.setOnClickListener {
                presenter.onSaveClick()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}