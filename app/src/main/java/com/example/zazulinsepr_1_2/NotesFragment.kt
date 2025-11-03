package com.example.zazulinsepr_1_2

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zazulinsepr_1_2.data.AppDatabase
import com.example.zazulinsepr_1_2.data.Note
import com.example.zazulinsepr_1_2.data.NoteDao
import com.example.zazulinsepr_1_2.ui.SimpleNotesAdapter
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonAdd: Button
    private lateinit var textEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvNotes)
        buttonAdd = view.findViewById(R.id.btnSaveNote)
        textEmpty = view.findViewById(R.id.tvEmpty)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val db = AppDatabase.getDatabase(requireContext())
        val noteDao = db.noteDao()

        observeNotes(noteDao)

        buttonAdd.setOnClickListener {
            showAddNoteDialog(noteDao)
        }
    }

    private fun observeNotes(noteDao: NoteDao) {
        noteDao.getAllNotes().observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) {
                textEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                textEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                recyclerView.adapter = SimpleNotesAdapter(notes) { note ->
                    showEditNoteDialog(noteDao, note)
                }
            }
        }
    }

    private fun showAddNoteDialog(noteDao: NoteDao) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_note, null)

        val editTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val editContent = dialogView.findViewById<EditText>(R.id.etNoteContent)

        AlertDialog.Builder(requireContext())
            .setTitle("Новая заметка")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { dialog, _ ->
                val title = editTitle.text.toString().trim()
                val content = editContent.text.toString().trim()

                if (title.isNotEmpty() && content.isNotEmpty()) {
                    addNewNote(noteDao, title, content)
                } else {
                    Toast.makeText(requireContext(), "Заполните оба поля", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showEditNoteDialog(noteDao: NoteDao, note: Note) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_note, null)

        val editTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val editContent = dialogView.findViewById<EditText>(R.id.etNoteContent)

        // Заполняем текущими данными
        editTitle.setText(note.title)
        editContent.setText(note.content)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Редактировать заметку")
            .setView(dialogView)
            .setPositiveButton("Сохранить", null)
            .setNeutralButton("Удалить", null)
            .setNegativeButton("Отмена", null)
            .create()

        dialog.setOnShowListener {
            val buttonSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val buttonDelete = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)

            buttonSave.setOnClickListener {
                val newTitle = editTitle.text.toString().trim()
                val newContent = editContent.text.toString().trim()

                if (newTitle.isEmpty() || newContent.isEmpty()) {
                    Toast.makeText(requireContext(), "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        val updatedNote = note.copy(title = newTitle, content = newContent)
                        noteDao.update(updatedNote)
                        Toast.makeText(requireContext(), "Заметка обновлена", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }

            buttonDelete.setOnClickListener {
                lifecycleScope.launch {
                    noteDao.delete(note)
                    Toast.makeText(requireContext(), "Заметка удалена", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun addNewNote(noteDao: NoteDao, title: String, content: String) {
        lifecycleScope.launch {
            val note = Note(title = title, content = content)
            noteDao.insert(note)
            Toast.makeText(requireContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show()
        }
    }
}
