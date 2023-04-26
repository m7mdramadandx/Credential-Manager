package com.example.credential_manager.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credential_manager.databinding.ItemTodoBinding


class TodoAdapter(
    private val onClickDelete: (Int) -> Unit
) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var todos = mutableListOf<Todo>()

    fun setTodos(data: List<Todo>) {
        todos.clear()
        todos.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(todos[position])

    override fun getItemCount(): Int = todos.size

    inner class ViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ctx: Context = binding.root.context

        fun bind(todo: Todo) {
            binding.apply {
//                if (layoutPosition % 2 == 0) {
//                    allahNameCard.setCardBackgroundColor(themeColor(R.color.colorSecondary, ctx))
//                } else {
//                    allahNameCard.setCardBackgroundColor(themeColor(R.color.colorPrimary, ctx))
//                }

                description.text = "${(layoutPosition + 1)}. ${todo.description}"

                delete.setOnClickListener { onClickDelete.invoke(todo.id) }
            }
        }

    }
}