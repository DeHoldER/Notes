package ru.geekbrains.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.geekbrains.notes.domain.Note;
import ru.geekbrains.notes.ui.NoteListAdapter;

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
//        return inflater.inflate(R.layout.fragment_note_list, container, false);

        View view = inflater.inflate(R.layout.note_list_recycler_view, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        NoteListAdapter adapter = new NoteListAdapter();
        recyclerView.setAdapter(adapter);

        List<Note> dataList = new NotesRepo().getNoteList();

        recyclerView.setAdapter(adapter);
        adapter.addData(dataList);
        adapter.notifyDataSetChanged();

        return view;
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        initList(view);

    }

    private void initPopupMenu(View view, Note note) {
        view.setOnLongClickListener((View.OnLongClickListener) v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);

            requireActivity().getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_do_something) {
                        Toast.makeText(requireContext(), "Do something with " + note.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    if (item.getItemId() == R.id.action_delete_note) {
                        Toast.makeText(requireContext(), "Delete note " + note.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            popupMenu.show();
            return true;


        });


    }

    // создаём список заметок на экране из массива
    private void initList(View view) {
        List<Note> noteList = new NotesRepo().getNoteList();
        LinearLayout noteListView = (LinearLayout) view;

        // В этом цикле создаём элементы View, заполняем заголовки, и добавляем на экран.
        // Кроме того, создаём обработку касания на элемент
        for (Note note : noteList) {

            View noteView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_note_title, noteListView, false);


            TextView noteTitle = noteView.findViewById(R.id.note_title);
            noteTitle.setText(note.getTitle());

            noteView.setOnClickListener(v -> showNoteDetails(note));
            initPopupMenu(noteView, note);

            noteListView.addView(noteView);

        }
    }

    private void showNoteDetails(Note note) {

        if (onNoteClicked != null) {
            onNoteClicked.onNoteClicked(note);
        }

    }

}
