package ru.geekbrains.notes;

import android.content.Context;
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
import java.util.Date;
import java.util.List;

import ru.geekbrains.notes.repository.LocalNotesRepository;

public class EditNoteFragment extends Fragment {

    public static EditNoteFragment newInstance(Note note) {
        EditNoteFragment fragment = new EditNoteFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NOTE, note);

        fragment.setArguments(bundle);
        return fragment;
    }


    private static final String ARG_NOTE = "ARG_NOTE";
    private Note note;

    private TextView titleView;
    private TextView textView;

    private int colorSelected = Note.COLOR_WHITE;
    private LocalNotesRepository localRepository;
    private Navigation navigation;
    private Publisher publisher;




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
        TextView dateView = view.findViewById(R.id.textView_date);

        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);

            titleView.setText(note.getTitle());
            textView.setText(note.getText());
            colorSelected = note.getColor();
            dateView.setText(new SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(note.getDate()));
        } else dateView.setText(new SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(new Date()));
        initColors(view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        navigation = mainActivity.getNavigation();
        publisher = mainActivity.getPublisher();
        localRepository = mainActivity.getLocalRepository();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getArguments() != null) {
            note.setTitle(titleView.getText().toString());
            note.setText(textView.getText().toString());
            note.setColor(colorSelected);
            note.setDate(new Date());
            localRepository.editNote(note);
//            publisher.notifySingle(note);
        } else {
            if (!textView.getText().toString().isEmpty()) {
                String id = "id" + (localRepository.getNoteListSize()+1);
                Note note = new Note(id, titleView.getText().toString(), textView.getText().toString(), colorSelected);
                localRepository.addNote(note);
//                publisher.notifySingle(note);
            }
        }
        publisher = null;
    }

    private void initColors(View view) {
        ImageView currentColor = view.findViewById(R.id.edit_color_current);

        if (note != null) {
            currentColor.setImageResource(new ColorManager(getResources()).getColorIdFromResourcesArray(note.getColor()));
        } else
            currentColor.setImageResource(new ColorManager(getResources()).getColorIdFromResourcesArray(Note.COLOR_WHITE));

        currentColor.setOnClickListener(v -> {
            currentColor.setVisibility(View.GONE);
            view.findViewById(R.id.colors_container).setVisibility(View.VISIBLE);
        });

        List<ImageView> colors = new ArrayList<>();

        colors.add(view.findViewById(R.id.edit_color_white));
        colors.add(view.findViewById(R.id.edit_color_green));
        colors.add(view.findViewById(R.id.edit_color_red));
        colors.add(view.findViewById(R.id.edit_color_blue));
        colors.add(view.findViewById(R.id.edit_color_yellow));
        colors.add(view.findViewById(R.id.edit_color_purple));

        for (int i = 0; i < colors.size(); i++) {
            int finalI = i;
            // ???????????? ???????????????? ???? ???????????? ?? ????????????????????
            colors.get(i).setOnClickListener(v -> {
                currentColor.setVisibility(View.VISIBLE);
                colorSelected = finalI;
                view.findViewById(R.id.colors_container).setVisibility(View.GONE);
                currentColor.setImageResource(new ColorManager(getResources()).getColorIdFromResourcesArray(colorSelected));
            });
        }
    }





}