package ru.geekbrains.notes.repository;

import java.util.List;

import ru.geekbrains.notes.Note;

public class NotesRepositoryImpl implements RepositoryManager {

    @Override
    public int getNoteListSize() {
        return RepoMock.NOTES.size();
    }

    @Override
    public Note getNote(int position) {
        return RepoMock.NOTES.get(position);
    }

    @Override
    public void addNote(Note note) {
        RepoMock.NOTES.add(note);
    }

    @Override
    public void removeNote(int position) {
        RepoMock.NOTES.remove(position);
    }

    @Override
    public List<Note> getNoteList() {
        return RepoMock.getNOTES();
    }

    @Override
    public void editNote(int position, Note note) {
        RepoMock.NOTES.set(position, note);
    }

}
