package ru.geekbrains.notes;

import java.util.ArrayList;
import java.util.List;

public class NotesRepository {

    private final List<Note> NOTES = new ArrayList<>();

    public NotesRepository() {
        fillList(100);
    }

    private void fillList(int noteNumber) {
        for (int i = 1; i < noteNumber + 1; i++) {
            String id = "id" + i;
            String title = "Заголовок № " + i;
            StringBuilder text = new StringBuilder();

            for (int j = 0; j < i; j++) {
                text.append("Lorem ipsum ");
            }
            NOTES.add(new Note(id, title, text.toString()));
        }
    }

    public int getNoteListSize() {
        return NOTES.size();
    }

    public Note getNote(int id) {
        return NOTES.get(id);
    }

    public List<Note> getNoteList() {
        return NOTES;
    }

    public void putNewNote(String id, String title, String text) {
        Note newNote = new Note(id, title, text);
        NOTES.add(newNote);
    }

}
