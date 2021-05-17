package ru.geekbrains.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.repository.NotesRepositoryImpl;
import ru.geekbrains.notes.repository.RepositoryManager;

public class EditNoteFragment extends Fragment {

//    private static final String ARG_NOTE = "ARG_NOTE";

//    public static NoteDetailsFragment newInstance(Note note) {
//        NoteDetailsFragment fragment = new NoteDetailsFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(ARG_NOTE, note);
//
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    RepositoryManager repositoryManager = new NotesRepositoryImpl();

    private TextView titleView;
    private TextView textView;
    private TextView dateView;

    private int colorSelected = Note.COLOR_WHITE;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleView = view.findViewById(R.id.editText_title);
        textView = view.findViewById(R.id.editText_text);
        dateView = view.findViewById(R.id.textView_date);

        List<ImageView> colors = new ArrayList<>();

        colors.add(view.findViewById(R.id.edit_color_white));
        colors.add(view.findViewById(R.id.edit_color_green));
        colors.add(view.findViewById(R.id.edit_color_red));
        colors.add(view.findViewById(R.id.edit_color_blue));
        colors.add(view.findViewById(R.id.edit_color_yellow));
        colors.add(view.findViewById(R.id.edit_color_purple));

        for (int i = 0; i < colors.size(); i++) {
            int finalI = i;
            colors.get(i).setOnClickListener(v -> {
                if (colorSelected != finalI) {
                colorSelected = finalI;
                    for (int j = 0; j < colors.size(); j++) {
                        if (j != finalI) {
                            colors.get(j).setVisibility(ImageView.GONE);
                        }
                    }
                } else {
                    for (int j = 0; j < colors.size(); j++) {
                        if (j != finalI) {
                            colors.get(j).setVisibility(ImageView.VISIBLE);
                        }
                    }
                }
            });
        }

//        colorWhite.setOnClickListener(v -> {
//            colorSelected = Note.COLOR_WHITE;
//            for (int i = 0; i < 5; i++) {
//                colors.get(i).setVisibility(ImageView.VISIBLE);
//            }
//        });

//        Note note = getArguments().getParcelable(ARG_NOTE);
//        titleView.setText(note.getTitle());
//        textView.setText(note.getText());
//        dateView.setText(new SimpleDateFormat("dd.MM.yyyy  -  hh:mm:ss").format(note.getDate()));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Note note = new Note("id_test", titleView.getText().toString(), textView.getText().toString(), colorSelected);
        repositoryManager.addNote(note);
    }
}