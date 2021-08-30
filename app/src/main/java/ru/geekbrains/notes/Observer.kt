package ru.geekbrains.notes

interface Observer {
    fun updateNoteData(note: Note?)
}