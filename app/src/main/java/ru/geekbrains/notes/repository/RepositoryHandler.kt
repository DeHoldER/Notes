package ru.geekbrains.notes.repository

import ru.geekbrains.notes.Note

interface RepositoryHandler {
    fun getNote(position: Int): Note?
    fun addNote(note: Note?)
    fun editNote(note: Note?)
    fun removeNote(position: Int)
    fun clear()
    val noteListSize: Int
    val noteList: List<Note?>?
    fun addNote(callback: Callback<Note?>?)
    fun getNoteList(callback: Callback<List<Note?>?>?)
}