package com.example.zazulinsepr_1_2.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Интерфейс для работы с таблицей заметок в базе данных.
 */
@Dao
interface NoteDao {

    // Получить все заметки как LiveData
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    // Добавить новую заметку
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    // Удалить заметку
    @Delete
    suspend fun delete(note: Note)

    // Найти заметку по ID
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): Note?
}
