package com.example.zazulinsepr_1_2.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zazulinsepr_1_2.R
import com.example.zazulinsepr_1_2.data.Note

class NotesAdapter(
    private val notes: List<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    /**
     * ViewHolder хранит ссылки на элементы одного элемента списка
     */
    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView =
            itemView.findViewById(R.id.tvTitle)
        val textContent: TextView =
            itemView.findViewById(R.id.tvContent)
    }
    // Создает новый элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }
    // Заполняет элемент списка данными
    override fun onBindViewHolder(holder: ViewHolder, position:
    Int) {
        val note = notes[position]
        holder.textTitle.text = note.title
        holder.textContent.text = note.content
// При клике на элемент - вызываем обработчик
        holder.itemView.setOnClickListener {
            onNoteClick(note)
        }
    }
    // Количество элементов в списке
    override fun getItemCount(): Int = notes.size
}