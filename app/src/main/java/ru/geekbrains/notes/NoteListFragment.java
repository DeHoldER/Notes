package ru.geekbrains.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class NoteListFragment extends Fragment {

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    private OnNoteClicked onNoteClicked;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnNoteClicked) {
            onNoteClicked = (OnNoteClicked) context;
        }
    }

    @Override
    public void onDetach() {
        onNoteClicked = null;
        super.onDetach();
    }

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    // создаём список заметок на экране из массива
    private void initList(View view) {
        List<Note> noteList = new NotesRepository().getNoteList();
        LinearLayout noteListView = (LinearLayout) view;

    // В этом цикле создаём элементы View, заполняем заголовки, и добавляем на экран.
    // Кроме того, создаём обработку касания на элемент
        for (Note note : noteList) {

            View noteView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_note, noteListView, false);

            TextView noteTitle = noteView.findViewById(R.id.note_title);
            noteTitle.setText(note.getTitle());

            noteView.setOnClickListener(v -> showNoteDetails(note));

            noteListView.addView(noteView);

//            Note note = noteList.get(i);
//            TextView textView = new TextView(getContext());
//            textView.setText(note.getTitle());
//            textView.setTextSize(20);
//            textView.setHeight(100);
//            layoutView.addView(textView);
//            textView.setOnClickListener(v -> {
//                showNoteDetails(note);
//            });

        }
    }

    private void showNoteDetails(Note note) {

        if (onNoteClicked != null) {
            onNoteClicked.onNoteClicked(note);
        }

//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction
//                .replace(R.id.fragment_container, NoteDetailsFragment.newInstance(note))
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                .addToBackStack("1")
//
//                .commit();
    }

}
