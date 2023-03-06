package com.example.todolistapp.feature_todo_list.presentation.todo_list

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R

class TodoListAdapter(
    private val onTodoClick: (ItemModel.TodoItem) -> Unit,
    private val onCompletedClick: (ItemModel.TodoItem, Boolean) -> Unit,
    private val onShowCompletedChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    private val entryList = mutableListOf<ItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val layout = when (viewType) {
            TYPE_TODO -> R.layout.item_todo
            TYPE_DIVIDER -> R.layout.item_todo_divider
            else -> throw IllegalArgumentException("Incorrect view type")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return TodoViewHolder(
            view,
            onItemClick = { onTodoClick(it) },
            onCheckboxChanged = { todo, completed -> onCompletedClick(todo, completed) },
            onDividerChecked = { onShowCompletedChanged(it) }
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(entryList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (entryList[position]) {
            is ItemModel.TodoItem -> TYPE_TODO
            is ItemModel.Divider -> TYPE_DIVIDER
        }
    }

    override fun getItemCount(): Int = entryList.size

    class TodoViewHolder(
        itemView: View,
        private val onItemClick: (ItemModel.TodoItem) -> Unit,
        private val onCheckboxChanged: (ItemModel.TodoItem, Boolean) -> Unit,
        private val onDividerChecked: (Boolean) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private fun bindTodo(item: ItemModel.TodoItem) {
            val layout = itemView.findViewById<ConstraintLayout>(R.id.clTodo)
            val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
            val textView = itemView.findViewById<TextView>(R.id.textView)

            layout.scaleX = 1f
            layout.scaleY = 1f
            layout.alpha = 1f

            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = item.isCompleted
            textView.text = item.text
            if (item.isCompleted) {
                textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                textView.setTextColor(itemView.resources.getColor(R.color.gray))
            } else {
                textView.setPaintFlags(0)
                textView.setTextColor(itemView.resources.getColor(R.color.black))
            }

            itemView.rootView.setOnClickListener { onItemClick(item) }
            checkBox.setOnCheckedChangeListener { compoundButton, b ->

                if (b) {
                    animateHideTodo(layout) { onCheckboxChanged(item, b) }
                } else onCheckboxChanged(item, b)
            }
        }

        private fun animateHideTodo(
            layout: ConstraintLayout,
            callback: () -> Unit
        ) {
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
            val alpha = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
            val hideView =
                ObjectAnimator.ofPropertyValuesHolder(layout, scaleX, scaleY, alpha)
                    .apply {
                        duration = 300
                    }

            AnimatorSet().apply {
                play(hideView)
                start()
                addListener(onEnd = {
                    callback()
                })
            }
        }

        private fun bindDivider(item: ItemModel.Divider) {
            val arrow = itemView.findViewById<ImageView>(R.id.ivArrow)
            val dividerText = itemView.findViewById<TextView>(R.id.tvDivider)

            if (item.isCompletedDisplayed) {
                arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                dividerText.text = itemView.resources.getString(R.string.hide_completed)
            } else {
                arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                dividerText.text = itemView.resources.getString(R.string.show_completed)
            }

            itemView.rootView.setOnClickListener {
                onDividerChecked(!item.isCompletedDisplayed)
            }
        }

        fun bind(itemModel: ItemModel) {
            when (itemModel) {
                is ItemModel.TodoItem -> bindTodo(itemModel)
                is ItemModel.Divider -> bindDivider(itemModel)
            }
        }
    }

    fun setEntries(entries: List<ItemModel>) {
        entryList.clear()
        entryList.addAll(entries)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_TODO = 100
        private const val TYPE_DIVIDER = 101
    }
}