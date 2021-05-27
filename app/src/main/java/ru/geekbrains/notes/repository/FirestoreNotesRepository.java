package ru.geekbrains.notes.repository;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.geekbrains.notes.Note;

public class FirestoreNotesRepository {

    public FirestoreNotesRepository(String email) {
        NOTES_COLLECTION = email;
    }

    private final FirebaseFirestore FIRE_STORE = FirebaseFirestore.getInstance();

    private static String NOTES_COLLECTION = "notes";
    private final static String TITLE = "title";
    private final static String TEXT = "text";
    private final static String CREATED_AT = "createdAt";
    private final static String COLOR = "color";
    private final static String ID = "id";


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getNoteList(Callback<List<Note>> callback) {
        FIRE_STORE.collection(NOTES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

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

                        tmp.sort(Comparator.comparing(Note::getDate));
                        callback.onSuccess(tmp);


                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void addNote(Note note, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();

        data.put(TITLE, note.getTitle());
        data.put(TEXT, note.getText());
        data.put(CREATED_AT, note.getDate());
        data.put(COLOR, note.getColor());
//        data.put(ID, note.getId());

        FIRE_STORE.collection(NOTES_COLLECTION)
                .add(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        note.setId(task.getResult().getId());
                        callback.onSuccess(note);

                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void editNote(Note note, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();

        data.put(TITLE, note.getTitle());
        data.put(TEXT, note.getText());
        data.put(CREATED_AT, note.getDate());
        data.put(COLOR, note.getColor());
        data.put(ID, note.getId());

        FIRE_STORE.collection(NOTES_COLLECTION)
                .document(note.getId())
                .set(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(note);

                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void removeNote(Note note, Callback<Note> callback) {

        FIRE_STORE.collection(NOTES_COLLECTION)
                .document(note.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(note);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void clearRepository(List<Note> noteList, Callback<Note> callback) {

        for (Note note : noteList) {
            FIRE_STORE.collection(NOTES_COLLECTION)
                    .document(note.getId())
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess(note);
                        } else {
                            callback.onError(task.getException());
                        }
                    });
        }


    }




}
