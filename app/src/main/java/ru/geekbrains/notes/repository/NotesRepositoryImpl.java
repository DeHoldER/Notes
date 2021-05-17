package ru.geekbrains.notes.repository;

import java.util.List;

import ru.geekbrains.notes.Note;

public class NotesRepositoryImpl implements RepositoryManager {

    @Override
    public int getNoteListSize() {
        return RepoMock.NOTES.size();
    }

    @Override
    public Note getNote(int id) {
        return RepoMock.NOTES.get(id);
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
    public void editNote(int position) {
        //todo
    }

}
