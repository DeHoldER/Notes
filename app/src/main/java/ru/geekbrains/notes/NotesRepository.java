package ru.geekbrains.notes;

import java.util.ArrayList;
import java.util.List;

public class NotesRepository {

    private final List<Note> NOTES = new ArrayList<>();

    public NotesRepository() {
        fillList(100);
    }

    private void fillList(int noteNumber) {

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
