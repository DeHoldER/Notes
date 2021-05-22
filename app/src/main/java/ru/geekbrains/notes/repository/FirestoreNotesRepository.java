package ru.geekbrains.notes.repository;

import java.util.List;

import ru.geekbrains.notes.Note;

public class FirestoreNotesRepository implements RepositoryManager{
    @Override
    public Note getNote(int position) {
        return null;
    }

    @Override
    public void addNote(Note note) {

    }

    @Override
    public void editNote(Note note) {

    }

    @Override
    public void removeNote(int position) {

    }

    @Override
    public void clear() {

    }

    @Override
    public int getNoteListSize() {
        return 0;
    }

    @Override
    public List<Note> getNoteList() {
        return null;
    }
}
