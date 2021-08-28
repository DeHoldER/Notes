package ru.geekbrains.notes.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import ru.geekbrains.notes.Note
import java.util.ArrayList
import java.util.Comparator
import java.util.HashMap

class FirestoreNotesRepository(email: String) {
    private val FIRE_STORE = FirebaseFirestore.getInstance()
    fun getNoteList(callback: Callback<MutableList<Note>>) {
        FIRE_STORE.collection(NOTES_COLLECTION)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    val tmp = ArrayList<Note>()
                    val docs = task.result!!.documents
                    for (doc in docs) {
                        val id = doc.id
                        val title = doc.getString(TITLE)
                        val text = doc.getString(TEXT)
                        val date = doc.getDate(CREATED_AT)
                        val color = doc.getLong(COLOR)!!.toInt()
                        tmp.add(Note(id, title, text, color, date))
                    }
                    tmp.sortWith(Comparator.comparing { obj: Note -> obj.date })
                    callback.onSuccess(tmp)
                } else {
                    callback.onError(task.exception)
                }
            }
    }

    fun addNote(note: Note, callback: Callback<Note>) {
        val data = HashMap<String, Any>()
        data[TITLE] = note.title
        data[TEXT] = note.text
        data[CREATED_AT] = note.date
        data[COLOR] = note.color
        //        data.put(ID, note.getId());
        FIRE_STORE.collection(NOTES_COLLECTION)
            .add(data)
            .addOnCompleteListener { task: Task<DocumentReference> ->
                if (task.isSuccessful) {
                    note.id = task.result!!.id
                    callback.onSuccess(note)
                } else {
                    callback.onError(task.exception)
                }
            }
    }

    fun editNote(note: Note, callback: Callback<Note>) {
        val data = HashMap<String, Any>()
        data[TITLE] = note.title
        data[TEXT] = note.text
        data[CREATED_AT] = note.date
        data[COLOR] = note.color
        data[ID] = note.id
        FIRE_STORE.collection(NOTES_COLLECTION)
            .document(note.id)
            .set(data)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    callback.onSuccess(note)
                } else {
                    callback.onError(task.exception)
                }
            }
    }

    fun removeNote(note: Note, callback: Callback<Note>) {
        FIRE_STORE.collection(NOTES_COLLECTION)
            .document(note.id)
            .delete()
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    callback.onSuccess(note)
                } else {
                    callback.onError(task.exception)
                }
            }
    }

    fun clearRepository(noteList: List<Note>, callback: Callback<Note>) {
        for (note in noteList) {
            FIRE_STORE.collection(NOTES_COLLECTION)
                .document(note.id)
                .delete()
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        callback.onSuccess(note)
                    } else {
                        callback.onError(task.exception)
                    }
                }
        }
    }

    companion object {
        private var NOTES_COLLECTION = "notes"
        private const val TITLE = "title"
        private const val TEXT = "text"
        private const val CREATED_AT = "createdAt"
        private const val COLOR = "color"
        private const val ID = "id"
    }

    init {
        NOTES_COLLECTION = email
    }
}