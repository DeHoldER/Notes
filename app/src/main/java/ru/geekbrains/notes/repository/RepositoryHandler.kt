package ru.geekbrains.notes.repository;

import java.util.List;

import ru.geekbrains.notes.Note;

public interface RepositoryHandler {

    Note getNote(int position);

    void addNote(Note note);

    void editNote(Note note);

    void removeNote(int position);

    void clear();

    int getNoteListSize();

    List<Note> getNoteList();

    void addNote(Callback<Note> callback);

    void getNoteList(Callback<List<Note>> callback);
}
