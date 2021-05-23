package ru.geekbrains.notes.repository;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.Note;

public class Mock {

    // Временное хранилище (эмулятор базы данных/сервера)

    private List<Note> NOTES = new ArrayList<>();

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

//    public static List<Note> getNOTES(){
//        return NOTES;
//    }
}
