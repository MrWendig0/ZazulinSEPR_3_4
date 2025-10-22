package com.example.zazulinsepr_1_2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Класс Note — это одна заметка в базе данных Room.
 *
 * @Entity(tableName = "notes") — создаёт таблицу с именем "notes".
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
