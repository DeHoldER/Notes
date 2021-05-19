package ru.geekbrains.notes.repository;

import java.util.List;

import ru.geekbrains.notes.Note;

public interface RepositoryManager {

    Note getNote(int position);

    void addNote(Note note);

    void editNote(int position, Note note);

    void removeNote(int position);

    void clear();

    int getNoteListSize();

    List<Note> getNoteList();

}
