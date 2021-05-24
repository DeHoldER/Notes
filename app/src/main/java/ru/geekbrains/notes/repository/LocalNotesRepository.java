package ru.geekbrains.notes.repository;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.Note;
import ru.geekbrains.notes.NoteListAdapter;

public class LocalNotesRepository {

    private List<Note> NOTES;
    private final FirestoreNotesRepository firestoreNotesRepository;
    private NoteListAdapter adapter;
    private RecyclerView recyclerView;

    public void setAdapter(NoteListAdapter adapter, RecyclerView recyclerView) {
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }

    public LocalNotesRepository() {
        NOTES = new ArrayList<>();
        firestoreNotesRepository = new FirestoreNotesRepository();
        firestoreNotesRepository.getNoteList(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> value) {
                NOTES = value;
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(NOTES.size());
            }
            @Override
            public void onError(Throwable error) {
                error.getCause();
            }
        });
    }

    public Note getNote(int position) {
        return NOTES.get(position);
    }

//    public void addNote(Note note) {
//        NOTES.add(note);
//    }

    public void addNote(Note note) {
        firestoreNotesRepository.addNote(note, new Callback<Note>() {
            @Override
            public void onSuccess(Note value) {
                NOTES.add(value);
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(NOTES.size());
            }
            @Override
            public void onError(Throwable error) {
                error.getCause();
            }
        });

    }

    public void editNote(Note note) {

        NOTES.set(NOTES.indexOf(note), note);
    }

    public void removeNote(int position) {
        NOTES.remove(position);
    }

    public void clear() {
        NOTES.clear();
    }

    public int getNoteListSize() {
        return NOTES.size();
    }

    public List<Note> getNoteList() {
        return NOTES;
    }

//    public void addNote(Callback<Note> callback) {
//
//    }

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
