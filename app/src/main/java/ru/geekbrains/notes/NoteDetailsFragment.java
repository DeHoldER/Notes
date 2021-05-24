package ru.geekbrains.notes;

import android.content.Context;
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

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;

public class NoteDetailsFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    private Navigation navigation;

    private Resources resources;

    Note note;

    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, note);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        navigation = activity.getNavigation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton button = view.findViewById(R.id.button_edit_note);
        TextView idView = view.findViewById(R.id.textView_id);
        TextView titleView = view.findViewById(R.id.textView_title);
        TextView dateView = view.findViewById(R.id.textView_date);
        TextView textView = view.findViewById(R.id.textView_text);
        ImageView color = view.findViewById(R.id.note_details_color);

        button.setOnClickListener(v -> {
            navigation.addFragment(EditNoteFragment.newInstance(note));
        });

        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }

        if (note != null) {
            color.setImageResource(new ColorManager(getResources()).getColorIdFromResourcesArray(note.getColor()));
            titleView.setText(note.getTitle());
            idView.setText(note.getId());
            textView.setText(note.getText());
            dateView.setText(new SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(note.getDate()));
        }
    }

}