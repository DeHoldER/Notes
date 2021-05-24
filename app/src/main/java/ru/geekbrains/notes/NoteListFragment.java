package ru.geekbrains.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.notes.repository.LocalNotesRepository;

public class NoteListFragment extends Fragment {

    private OnNoteClicked onNoteClicked;
    private NoteListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private LocalNotesRepository localRepository;
    private RecyclerView recyclerView;
    private Publisher publisher;
    private Navigation navigation;
    private MainActivity mainActivity;

    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_list_fragment, container, false);
        Context context = view.getContext();

        initView(view, context);

        // Установим слушателя
        adapter.SetOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(View v, int position, View itemView) {
//                showPopupMenu(v, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView.showContextMenu(5, 10);
                }
            }

            @Override
            public void onItemClick(View view, int position) {
                showNoteDetails(localRepository.getNote(position));
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
        mainActivity = (MainActivity)context;

        publisher = mainActivity.getPublisher();
        navigation = mainActivity.getNavigation();
        localRepository = mainActivity.getLocalRepository();

    }

    @Override
    public void onDetach() {
        onNoteClicked = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(localRepository.getNoteListSize());
        mainActivity.throwListView(adapter, recyclerView);
    }

//    private void showPopupMenu(View view, int position) {
//        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
//        requireActivity().getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
//
//        popupMenu.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.action_edit_note) {
////                localRepository.editNote(position);
//                linearLayoutManager.scrollToPosition(repositoryManager.getNoteListSize());
//                Toast.makeText(requireContext(), "Do something with " + repositoryManager.getNote(position).getTitle(), Toast.LENGTH_SHORT).show();
//            }
//            if (item.getItemId() == R.id.action_delete_note) {
//                repositoryManager.removeNote(position);
//                adapter.notifyItemRemoved(position);
//            }
//            if (item.getItemId() == R.id.action_clear) {
//                repositoryManager.clear();
//                adapter.notifyDataSetChanged();
//            }
//            return true;
//        });
//        popupMenu.show();
//    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.action_edit_note) {

            navigation.addFragment(EditNoteFragment.newInstance(localRepository.getNote(position)));


//            publisher.subscribe(new Observer() {
//                @Override
//                public void updateNoteData(Note note) {
//                int position = 0;
//
//                    for (int i = 0; i < repositoryManager.getNoteListSize(); i++) {
//                        if (repositoryManager.getNote(i).equals(note)) {
//                            position = i;
//                        }
//                    }
//
//                    repositoryManager.editNote(note);
//                    adapter.notifyItemChanged(position);
//                    recyclerView.smoothScrollToPosition(repositoryManager.getNoteListSize() - 1);
//                }
//            });

            linearLayoutManager.scrollToPosition(localRepository.getNoteListSize());
        }
        if (item.getItemId() == R.id.action_delete_note) {
            localRepository.removeNote(adapter.getMenuPosition());
            adapter.notifyItemRemoved(adapter.getMenuPosition());
        }
        if (item.getItemId() == R.id.action_clear) {
            localRepository.clear();
            adapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
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

        adapter = new NoteListAdapter(this);
        adapter.setResources(getResources());
        adapter.notifyDataSetChanged();

        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        MainActivity mainActivity = (MainActivity)context;
//        mainActivity.throwListView(adapter, recyclerView);
    }
}
