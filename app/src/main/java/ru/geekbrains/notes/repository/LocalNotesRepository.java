package ru.geekbrains.notes.repository;

import android.os.Build;

import androidx.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LocalNotesRepository() {
        NOTES = new ArrayList<>();
        firestoreNotesRepository = new FirestoreNotesRepository();
        syncList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void syncList() {
        firestoreNotesRepository.getNoteList(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> value) {
                if (value.size() > NOTES.size()) {
                    NOTES = value;
//                adapter.notifyItemRangeInserted(0, NOTES.size());
                    adapter.notifyItemInserted(NOTES.size());
                    recyclerView.smoothScrollToPosition(NOTES.size());
                } else {
                    NOTES = value;
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(NOTES.size()-1);
                }
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


    public void addNote(Note note) {
        firestoreNotesRepository.addNote(note, new Callback<Note>() {
            @Override
            public void onSuccess(Note value) {
                NOTES.add(value);
//                adapter.notifyItemInserted(NOTES.size());
                recyclerView.smoothScrollToPosition(NOTES.size());
            }
            @Override
            public void onError(Throwable error) {
                error.getCause();
            }
        });

    }

    public void editNote(Note note) {
        firestoreNotesRepository.editNote(note, new Callback<Note>() {
            @Override
            public void onSuccess(Note value) {

                int newPosition = 0;

                for (int i = 0; i < NOTES.size(); i++) {
                    if (NOTES.get(i).getId().equals(value.getId())) {
                        newPosition = i;
                    }
                }

                NOTES.set(newPosition, note);

            }
            @Override
            public void onError(Throwable error) {
                error.getCause();
            }
        });
    }

    public void removeNote(int position) {
        firestoreNotesRepository.removeNote(NOTES.get(position).getNote(), new Callback<Note>() {
            @Override
            public void onSuccess(Note value) {
                NOTES.remove(position);
                adapter.notifyItemRemoved(position);
            }
            @Override
            public void onError(Throwable error) {
                error.getCause();
            }
        });
    }

    public void clear() {
        firestoreNotesRepository.clearRepository(NOTES, new Callback<Note>() {
            @Override
            public void onSuccess(Note value) {
                NOTES.remove(value);
                adapter.notifyItemRemoved(0);
            }
            @Override
            public void onError(Throwable error) {
                error.getCause();
            }
        });
    }

    public int getNoteListSize() {
        return NOTES.size();
    }


    public void fillList(int numberOfAdditionallyGeneratedNotes) {

        addNote(new Note("id1", "Заметка № 1",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_GREEN));
        addNote(new Note("id2", "Заметка № 2",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_GREEN));
        addNote(new Note("id3", "Заметка № 3",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_BLUE));
        addNote(new Note("id4", "Заметка № 4",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_YELLOW));
        addNote(new Note("id5", "Заметка № 5",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_PURPLE));

        for (int i = 6; i < numberOfAdditionallyGeneratedNotes+1; i++) {
            String id = "id" + i;
            String title = "Заметка № " + i;
            StringBuilder text = new StringBuilder();

            for (int j = 0; j < i; j++) {
                text.append("Lorem ipsum ");
            }
            addNote(new Note(id, title, text.toString()));
        }
    }

}
