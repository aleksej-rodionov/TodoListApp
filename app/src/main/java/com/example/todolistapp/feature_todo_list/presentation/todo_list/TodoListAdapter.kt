package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.todolistapp.databinding.ItemTodoBinding
import com.example.todolistapp.feature_todo_list.domain.model.Todo

class TodoListAdapter(
    private val context: Context,
    private val onTodoClick: (Todo) -> Unit,
    private val onCompletedClick: (Todo, Boolean) -> Unit
) : ListAdapter<ItemModel, TodoListAdapter.TodoViewHolder>(TodoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(
            binding,
            onItemClick = {
                val todo = getItem(it)
                if (todo != null) {
                    onTodoClick(todo)
                }
            },
            onCheckboxChanged = { pos, completed ->
                val todo = getItem(pos)
                todo?.let { t ->
                    onCompletedClick(t, completed)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curItem = getItem(position)
        if (curItem != null) holder.bindTodo(curItem)
    }

    inner class TodoViewHolder(
        private val binding: ViewBinding,
        private val onItemClick: (Int) -> Unit,
        private val onCheckboxChanged: (Int, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTodo(todo: ItemModel.TodoItem) {
            binding.apply {
                checkBox.isChecked = todo.isCompleted
                textView.text = todo.text
            }
        }

        fun bindDivider(divider: ItemModel.Divider) {
            binding/appl
        }

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClick(position)
                    }
                }

                checkBox.setOnCheckedChangeListener { compoundButton, b ->
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onCheckboxChanged(position, b)
                    }
                }
            }
        }
    }

    class TodoComparator : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return when (oldItem) {
                is ItemModel.TodoItem -> {
                    when (newItem) {
                        is ItemModel.TodoItem -> oldItem.id == newItem.id
                        is ItemModel.Divider -> false
                    }
                }
                is ItemModel.Divider -> {
                    when (newItem) {
                        is ItemModel.TodoItem -> false
                        is ItemModel.Divider -> true
                    }
                }
            }
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel) =
            oldItem == newItem
    }
}