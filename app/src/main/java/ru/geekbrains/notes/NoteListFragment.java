package ru.geekbrains.notes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private static final String ARG_EMAIL = "ARG_EMAIL";
    private String email;
    private OnNoteClicked onNoteClicked;
    private NoteListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private LocalNotesRepository localRepository;
    private RecyclerView recyclerView;
    private Publisher publisher;
    private Navigation navigation;
    private MainActivity mainActivity;
    private boolean alertDialogAnswer = false;

    public static NoteListFragment newInstance(String email) {
        NoteListFragment fragment = new NoteListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_EMAIL, email);

        fragment.setArguments(bundle);
        return fragment;
    }



    public interface OnNoteClicked {
        void onNoteClicked(Note note);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }

        if (context instanceof OnNoteClicked) {
            onNoteClicked = (OnNoteClicked) context;
        }
        mainActivity = (MainActivity) context;

        publisher = mainActivity.getPublisher();
        navigation = mainActivity.getNavigation();
        mainActivity.initLocalRepository(email);
        localRepository = mainActivity.getLocalRepository();
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

    @Override
    public void onResume() {
        super.onResume();

//        recyclerView.scrollToPosition(localRepository.getNoteListSize());

        mainActivity.throwRecyclerView(adapter, recyclerView);
        if (localRepository != null) {
            localRepository.syncList();
        }

    }

    @Override
    public void onDetach() {
        onNoteClicked = null;
        publisher = null;
        super.onDetach();
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
//            adapter.notifyItemRemoved(adapter.getMenuPosition());
        }
        if (item.getItemId() == R.id.action_clear) {
            showAlertDialog();
        }
        return super.onContextItemSelected(item);
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        builder.setTitle(R.string.alert_dialog)
                .setMessage(R.string.alert_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.alert_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localRepository.clear();
                    }
                })
                .setNegativeButton(R.string.alert_button_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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

        mainActivity = (MainActivity) context;
//        mainActivity.throwListView(adapter, recyclerView);
    }
}
