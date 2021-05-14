package ru.geekbrains.notes.domain;

import java.util.ArrayList;
import java.util.List;

public class MockNotesRepository implements NotesRepository {
    @Override
    public List<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        notes.add(new Note("id1","Title 1","ASDASD ASD Aasd as as aaw da daw dsas da dasS DS"));
        notes.add(new Note("id2","Title 2","ASDASD ASD Aasd as as asas das dasdas dasd as a dasS DS"));
        notes.add(new Note("id3","Title 3","ASDASD ASD Aasd as as asas da dasd asd asS DS"));
        notes.add(new Note("id4","Title 4","ASDASD ASD Aasd as as asas da das dasd asS DS"));
        notes.add(new Note("id5","Title 5","ASDASD ASD Aasd as as asas da dasS DS"));
        notes.add(new Note("id6","Title 6","ASDASD ASD Aasd as as asas da sdas d aa dasS DS"));
        notes.add(new Note("id7","Title 7","ASDASD ASD Aasd as as asas asd as da dasS DS"));
        notes.add(new Note("id8","Title 8","ASDASD ASD Aasd as as asas da dasS DS"));
        notes.add(new Note("id9","Title 9","ASDASD ASD Aasd as as asas a sd asd asd as dasd asd as da asd asd as da dasS DS"));
        notes.add(new Note("id10","Title 10","ASDASD ASD Aasd as as asas dad as dasd as ddasd asd  dasS DS"));
        notes.add(new Note("id11","Title 11","ASDASD ASD Aasd as as asd das das fdhsr hsryj wjy wk jek tjsryjsryj sryj sryj sryjsyj syj sr jys sy asas da dasS DS"));

        return notes;
    }
}
