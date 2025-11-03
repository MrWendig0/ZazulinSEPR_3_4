package com.example.zazulinsepr_1_2.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Интерфейс NoteDao — для доступа к данным в таблице "notes".
 *
 * Аннотация @Dao сообщает Room, что этот интерфейс используется для SQL операций.
 */
@Dao
interface NoteDao {

    /**
     * Добавление новой заметки.
     */
    @Insert
    suspend fun insert(note: Note)

    /**
     * Удаление заметки.
     */
    @Delete
    suspend fun delete(note: Note)

    /**
     * Обновление существующей заметки.
     */
    @Update
    suspend fun update(note: Note)

    /**
     * Получение всех заметок, отсортированных по дате (от новых к старым).
     */
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): LiveData<List<Note>>
}
