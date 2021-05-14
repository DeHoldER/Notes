package ru.geekbrains.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.geekbrains.notes.domain.Note;

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

        Note note = getArguments().getParcelable(ARG_NOTE);
        titleView.setText(note.getTitle());
        dateView.setText(note.getDate().toString());
        textView.setText(note.getText());
    }
}