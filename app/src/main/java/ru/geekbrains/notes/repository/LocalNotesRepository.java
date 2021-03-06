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
    public LocalNotesRepository(String email) {
        NOTES = new ArrayList<>();
        firestoreNotesRepository = new FirestoreNotesRepository(email);
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
                    recyclerView.smoothScrollToPosition(NOTES.size());
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
//                recyclerView.smoothScrollToPosition(NOTES.size());
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

        addNote(new Note("id1", "?????????????? ??? 1",
                "???????????????? ???????????????? ???????????????????? ?? ???????????????????????????? ????????????, ???????? ???? ?????? ???? ?????????????? ??????. ???????????????? ?????????????????? ?????????? ??????????????????????, ?????????? ?????????????????????? ?????????? ?????????????? ?????????? ??????????????????????",
                Note.COLOR_GREEN));
        addNote(new Note("id2", "?????????????? ??? 2",
                "???????????????? ???????????????? ???????????????????? ?? ???????????????????????????? ????????????, ???????? ???? ?????? ???? ?????????????? ??????. ???????????????? ?????????????????? ?????????? ??????????????????????, ?????????? ?????????????????????? ?????????? ?????????????? ?????????? ??????????????????????",
                Note.COLOR_GREEN));
        addNote(new Note("id3", "?????????????? ??? 3",
                "???????????????? ???????????????? ???????????????????? ?? ???????????????????????????? ????????????, ???????? ???? ?????? ???? ?????????????? ??????. ???????????????? ?????????????????? ?????????? ??????????????????????, ?????????? ?????????????????????? ?????????? ?????????????? ?????????? ??????????????????????",
                Note.COLOR_BLUE));
        addNote(new Note("id4", "?????????????? ??? 4",
                "???????????????? ???????????????? ???????????????????? ?? ???????????????????????????? ????????????, ???????? ???? ?????? ???? ?????????????? ??????. ???????????????? ?????????????????? ?????????? ??????????????????????, ?????????? ?????????????????????? ?????????? ?????????????? ?????????? ??????????????????????",
                Note.COLOR_YELLOW));
        addNote(new Note("id5", "?????????????? ??? 5",
                "???????????????? ???????????????? ???????????????????? ?? ???????????????????????????? ????????????, ???????? ???? ?????? ???? ?????????????? ??????. ???????????????? ?????????????????? ?????????? ??????????????????????, ?????????? ?????????????????????? ?????????? ?????????????? ?????????? ??????????????????????",
                Note.COLOR_PURPLE));

        for (int i = 6; i < numberOfAdditionallyGeneratedNotes+1; i++) {
            String id = "id" + i;
            String title = "?????????????? ??? " + i;
            StringBuilder text = new StringBuilder();

            for (int j = 0; j < i; j++) {
                text.append("Lorem ipsum ");
            }
            addNote(new Note(id, title, text.toString()));
        }
    }

}
