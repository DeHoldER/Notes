package ru.geekbrains.notes.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.geekbrains.notes.Note;

public class FirestoreNotesRepository implements RepositoryHandler {

    private final FirebaseFirestore FIRE_STORE = FirebaseFirestore.getInstance();

    private final static String NOTES_COLLECTION = "notes";
    private final static String TITLE = "title";
    private final static String TEXT = "text";
    private final static String CREATED_AT = "CreatedAt";
    private final static String COLOR = "color";

    @Override
    public Note getNote(int position) {
        return null;
    }

    @Override
    public void addNote(Note note) {

    }

    @Override
    public void editNote(Note note) {

    }

    @Override
    public void removeNote(int position) {

    }

    @Override
    public void clear() {

    }

    @Override
    public int getNoteListSize() {
        return 0;
    }

    @Override
    public List<Note> getNoteList() {
        return null;
    }

    @Override
    public void addNote(Callback<Note> callback) {

    }

    @Override
    public void getNoteList(Callback<List<Note>> callback) {
        FIRE_STORE.collection(NOTES_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            ArrayList<Note> tmp = new ArrayList<>();

                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                String id = doc.getId();
                                String title = doc.getString(TITLE);
                                String text = doc.getString(TEXT);
                                Date date = doc.getDate(CREATED_AT);
                                int color = doc.getLong(COLOR).intValue();

                                tmp.add(new Note(id, title, text, color, date));
                            }

                            callback.onSuccess(tmp);


                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });
    }


}
