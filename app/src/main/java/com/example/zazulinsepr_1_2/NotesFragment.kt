package com.example.zazulinsepr_1_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.zazulinsepr_1_2.data.Note
import com.example.zazulinsepr_1_2.data.NoteDao
import com.example.zazulinsepr_1_2.viewmodel.NotesAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewEmpty: TextView
    override fun onViewCreated(view: View, savedInstanceState:
    Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Находим View элементы по ID
        recyclerView = view.findViewById(R.id.rvNotes)
        textViewEmpty = view.findViewById(R.id.tvEmpty)
        val buttonAdd = view.findViewById<Button>(R.id.btnAddNote)
// Получаем доступ к базе данных
        val database = AppDatabase.getDatabase(requireContext())
        val noteDao = database.noteDao()
// 1. Настраиваем кнопку добавления
        buttonAdd.setOnClickListener {
            addNewNote(noteDao)
        }
// 2. Показываем список заметок

        showNotesList(noteDao)

    }
    /**
     * Добавляет новую тестовую заметку в базу
     */
    private fun addNewNote(noteDao: NoteDao) {
// Запускаем в фоновом потоке (база данных не может работать в основном потоке UI)
        lifecycleScope.launch {
// Создаем новую заметку
            val note = Note(
                title = "Заметка от {System.currentTimeMillis()}",
            content = "Это тестовое содержимое заметки"
            )
// Добавляем в базу
            noteDao.insert(note)
// Показываем сообщение
            Toast.makeText(requireContext(), "Заметка добавлена!", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Показывает список заметок и автоматически обновляет его
     */
    private fun showNotesList(noteDao: NoteDao) {
// Наблюдаем за изменениями в базе данных
        noteDao.getAllNotes().observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) {
// Если заметок нет - показываем текст "пусто"
                textViewEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE

            } else {
// Если есть заметки - показываем список
                textViewEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
// Создаем и настраиваем адаптер

                val adapter = NotesAdapter(notes) { note ->
// Обработчик клика - удаляем заметку
                    deleteNote(noteDao, note)
                }
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
    /**
     * Удаляет заметку при клике на нее
     */
    private fun deleteNote(noteDao: NoteDao, note: Note) {
        lifecycleScope.launch {
            noteDao.delete(note)
            Toast.makeText(requireContext(), "Заметка удалена",
                Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}