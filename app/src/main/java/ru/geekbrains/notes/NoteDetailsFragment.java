package ru.geekbrains.notes;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

import ru.geekbrains.notes.repository.NotesRepositoryImpl;
import ru.geekbrains.notes.repository.RepositoryManager;

public class NoteDetailsFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, note);

        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleView = view.findViewById(R.id.textView_title);
        TextView dateView = view.findViewById(R.id.textView_date);
        TextView textView = view.findViewById(R.id.textView_text);
        ImageView color = view.findViewById(R.id.note_details_color);


        Note note = null;
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }

        if (note != null) {
            color.setImageResource(new ColorManager(getResources()).getColorIdFromResourcesArray(note.getColor()));
            titleView.setText(note.getTitle());
            textView.setText(note.getText());
            dateView.setText(new SimpleDateFormat("dd.MM.yyyy  -  hh:mm:ss").format(note.getDate()));
        }
    }
}