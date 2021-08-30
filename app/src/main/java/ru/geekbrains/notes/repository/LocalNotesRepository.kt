package ru.geekbrains.notes.repository

import androidx.annotation.RequiresApi
import android.os.Build
import ru.geekbrains.notes.NoteListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.notes.Note
import java.lang.StringBuilder
import java.util.ArrayList

class LocalNotesRepository @RequiresApi(api = Build.VERSION_CODES.N) constructor(email: String?) {
    private var NOTES: MutableList<Note>
    private val firestoreNotesRepository: FirestoreNotesRepository
    private var adapter: NoteListAdapter? = null
    private var recyclerView: RecyclerView? = null
    fun setAdapter(adapter: NoteListAdapter?, recyclerView: RecyclerView?) {
        this.adapter = adapter
        this.recyclerView = recyclerView
    }

    fun syncList() {
        firestoreNotesRepository.getNoteList(object : Callback<MutableList<Note>> {
            override fun onSuccess(value: MutableList<Note>) {
                if (value.size > NOTES.size) {
                    NOTES = value
                    //                adapter.notifyItemRangeInserted(0, NOTES.size());
                    adapter!!.notifyItemInserted(NOTES.size)
                    //                    adapter.notifyDataSetChanged();
                    recyclerView!!.smoothScrollToPosition(NOTES.size)
                } else {
                    NOTES = value
                    adapter!!.notifyDataSetChanged()
                    recyclerView!!.smoothScrollToPosition(NOTES.size)
                }
            }

            override fun onError(error: Throwable?) {
                error!!.cause
            }

        })
    }

    fun getNote(position: Int): Note {
        return NOTES[position]
    }

    fun addNote(note: Note?) {
        firestoreNotesRepository.addNote(note!!, object : Callback<Note> {
            override fun onSuccess(value: Note) {
                NOTES.add(value)
                //                adapter.notifyItemInserted(NOTES.size());
//                recyclerView.smoothScrollToPosition(NOTES.size());
            }

            override fun onError(error: Throwable?) {
                error!!.cause
            }
        })
    }

    fun editNote(note: Note) {
        firestoreNotesRepository.editNote(note, object : Callback<Note> {
            override fun onSuccess(value: Note) {
                var newPosition = 0
                for (i in NOTES.indices) {
                    if (NOTES[i].id == value.id) {
                        newPosition = i
                    }
                }
                NOTES[newPosition] = note
            }

            override fun onError(error: Throwable?) {
                error!!.cause
            }
        })
    }

    fun removeNote(position: Int) {
        firestoreNotesRepository.removeNote(NOTES[position].note, object : Callback<Note> {
            override fun onSuccess(value: Note) {
                NOTES.removeAt(position)
                adapter!!.notifyItemRemoved(position)
            }

            override fun onError(error: Throwable?) {
                error!!.cause
            }
        })
    }

    fun clear() {
        firestoreNotesRepository.clearRepository(NOTES, object : Callback<Note> {
            override fun onSuccess(value: Note) {
                NOTES.remove(value)
                adapter!!.notifyItemRemoved(0)
            }

            override fun onError(error: Throwable?) {
                error!!.cause
            }
        })
    }

    val noteListSize: Int
        get() = NOTES.size

    fun fillList(numberOfAdditionallyGeneratedNotes: Int) {
        addNote(
            Note(
                "id1", "Заметка № 1",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_GREEN
            )
        )
        addNote(
            Note(
                "id2", "Заметка № 2",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_GREEN
            )
        )
        addNote(
            Note(
                "id3", "Заметка № 3",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_BLUE
            )
        )
        addNote(
            Note(
                "id4", "Заметка № 4",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_YELLOW
            )
        )
        addNote(
            Note(
                "id5", "Заметка № 5",
                "Сделайте фрагмент добавления и редактирования данных, если вы ещё не сделали его. Сделайте навигацию между фрагментами, также организуйте обмен данными между фрагментами",
                Note.COLOR_PURPLE
            )
        )
        for (i in 6 until numberOfAdditionallyGeneratedNotes + 1) {
            val id = "id$i"
            val title = "Заметка № $i"
            val text = StringBuilder()
            for (j in 0 until i) {
                text.append("Lorem ipsum ")
            }
            addNote(Note(id, title, text.toString()))
        }
    }

    init {
        NOTES = ArrayList()
        firestoreNotesRepository = FirestoreNotesRepository(email!!)
        syncList()
    }
}