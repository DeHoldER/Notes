package ru.geekbrains.notes.repository;

import java.util.List;

import ru.geekbrains.notes.Note;

public class NotesRepositoryImpl implements RepositoryManager {

    @Override
    public Note getNote(int position) {
        return RepoMock.NOTES.get(position);
    }

    @Override
    public void addNote(Note note) {
        RepoMock.NOTES.add(note);
    }

    @Override
    public void editNote(Note note) {

        RepoMock.NOTES.set(RepoMock.NOTES.indexOf(note), note);
    }

    @Override
    public void removeNote(int position) {
        RepoMock.NOTES.remove(position);
    }

    @Override
    public void clear() {
        RepoMock.NOTES.clear();
    }

    @Override
    public int getNoteListSize() {
        return RepoMock.NOTES.size();
    }

    @Override
    public List<Note> getNoteList() {
        return RepoMock.getNOTES();
    }



}
