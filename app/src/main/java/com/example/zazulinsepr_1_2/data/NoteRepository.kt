package com.example.zazulinsepr_1_2.data

/**
 * Репозиторий — прослойка между базой данных и ViewModel.
 * Здесь вызываются DAO-функции, но можно добавлять и логику (например, фильтрацию).
 */
class NoteRepository(private val noteDao: NoteDao) {

    // Получить все заметки
    suspend fun getAllNotes() = noteDao.getAllNotes()

    // Добавить заметку
    suspend fun insert(note: Note) = noteDao.insert(note)

    // Удалить заметку
    suspend fun delete(note: Note) = noteDao.delete(note)

    // Получить заметку по ID
    suspend fun getNoteById(id: Long) = noteDao.getNoteById(id)
}
