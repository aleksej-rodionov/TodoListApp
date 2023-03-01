package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.databinding.ItemTodoBinding
import com.example.todolistapp.feature_todo_list.domain.model.Todo

class TodoListAdapter(
    private val context: Context,
    private val onTodoClick: (Todo) -> Unit,
    private val onCompletedClick: (Todo, Boolean) -> Unit
) : ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoComparator()) {

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
        if (curItem != null) holder.bind(curItem)
    }

    inner class TodoViewHolder(
        private val binding: ItemTodoBinding,
        private val onItemClick: (Int) -> Unit,
        private val onCheckboxChanged: (Int, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.apply {
                checkBox.isChecked = todo.isCompleted
                textView.text = todo.text
            }
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

    class TodoComparator : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem == newItem
    }
}