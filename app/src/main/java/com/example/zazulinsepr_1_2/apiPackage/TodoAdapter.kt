package com.example.zazulinsepr_1_2.apiPackage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zazulinsepr_1_2.R

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    private var todos: List<Todo> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textCompleted: TextView = itemView.findViewById(R.id.textCompleted)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todos[position]
        holder.textTitle.text = todo.title
        holder.textCompleted.text = if (todo.completed) "Выполнена" else "Не выполнена"
        holder.checkBox.isChecked = todo.completed
        holder.textCompleted.setTextColor(
            if (todo.completed) Color.parseColor("#4CAF50") else Color.parseColor("#F44336")
        )
    }

    override fun getItemCount(): Int = todos.size

    fun submitList(newTodos: List<Todo>) {
        todos = newTodos
        notifyDataSetChanged()
    }
}
