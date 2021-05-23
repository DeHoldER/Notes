package ru.geekbrains.notes.repository;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.Note;

public class LocalNotesRepository implements RepositoryHandler {

    private List<Note> NOTES;

    public LocalNotesRepository() {
        NOTES = new ArrayList<>();
    }

    @Override
    public Note getNote(int position) {
        return NOTES.get(position);
    }

    @Override
    public void addNote(Note note) {
        NOTES.add(note);
    }

    @Override
    public void editNote(Note note) {

        NOTES.set(NOTES.indexOf(note), note);
    }

    @Override
    public void removeNote(int position) {
        NOTES.remove(position);
    }

    @Override
    public void clear() {
        NOTES.clear();
    }

    @Override
    public int getNoteListSize() {
        return NOTES.size();
    }

    @Override
    public List<Note> getNoteList() {
        return NOTES;
    }

    @Override
    public void addNote(Callback<Note> callback) {

    }

    @Override
    public void getNoteList(Callback<List<Note>> callback) {

    }


    public void fillList(int noteNumber) {

        NOTES.add(new Note("id1", "Заметка № 1",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_GREEN));
        NOTES.add(new Note("id2", "Заметка № 2",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_GREEN));
        NOTES.add(new Note("id3", "Заметка № 3",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_BLUE));
        NOTES.add(new Note("id4", "Заметка № 4",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_YELLOW));
        NOTES.add(new Note("id5", "Заметка № 5",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_PURPLE));

        for (int i = 6; i < noteNumber + 1; i++) {
            String id = "id" + i;
            String title = "Заметка № " + i;
            StringBuilder text = new StringBuilder();

            for (int j = 0; j < i; j++) {
                text.append("Lorem ipsum ");
            }
            NOTES.add(new Note(id, title, text.toString()));
        }
    }

}
