package ru.geekbrains.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.notes.repository.NotesRepositoryImpl;
import ru.geekbrains.notes.repository.RepositoryManager;

public class NoteListFragment extends Fragment {

    private OnNoteClicked onNoteClicked;
    private NoteListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RepositoryManager repositoryManager;
    private RecyclerView recyclerView;

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_list_fragment, container, false);
        Context context = view.getContext();

        repositoryManager = new NotesRepositoryImpl();

        initView(view, context);

        // Установим слушателя
        adapter.SetOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                showPopupMenu(v, position);
            }

            @Override
            public void onItemClick(View view, int position) {
                showNoteDetails(repositoryManager.getNote(position));
            }

        });
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }

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

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(repositoryManager.getNoteListSize());
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        requireActivity().getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_note) {
//                repositoryManager.editNote(position);
                linearLayoutManager.scrollToPosition(repositoryManager.getNoteListSize());
                Toast.makeText(requireContext(), "Do something with " + repositoryManager.getNote(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
            if (item.getItemId() == R.id.action_delete_note) {
                repositoryManager.removeNote(position);
                adapter.notifyItemRemoved(position);
            }
            return true;
        });
        popupMenu.show();
    }

    private void showNoteDetails(Note note) {
        if (onNoteClicked != null) {
            onNoteClicked.onNoteClicked(note);
        }
    }

    private void initView(View view, Context context) {
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        adapter = new NoteListAdapter();
        adapter.setResources(getResources());
        adapter.notifyDataSetChanged();

        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
