package ru.geekbrains.notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesRepository {

    private List<Note> NOTES= new ArrayList<>();

    public NotesRepository() {
        NOTES.add(new Note("Заголовок 1", "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum roivjerioerjvoierj jirj ijerioverj ejio jetio je o"));
        NOTES.add(new Note("Заголовок 2", "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum roivjerioerjvoierj jirj ijerioverj ejio jetio je o w wev wporb eotmro mnoety otm nepym pymn pmn pmwpnm"));
        NOTES.add(new Note("Заголовок 3", " oth opwhw prohw porj wprowjrypo ojet[pjkep[typ jskp[ kap[ekh[ atr hjtrj rmy;pj mktr;j mtrmhfgposrj srj kwrp to jkstr prtsokj rjkapkh apoetj art kjtrsyk [psyk p[srkt jkrstojkr"));
        NOTES.add(new Note("Заголовок 4", "reg;okerpgh epro hkpoetk hporpejyje jpeky jwwtjq ]rkpkj ]wrkj wrtk ha[erh jkae[tj k[rpkjp[k j[kj [tk pekat thjartphjtrso; "));
        NOTES.add(new Note("Заголовок 5", "ТЕКСТ ЗАМЕТКИ 3!!!!!!"));
    }

    public int getNoteListSize() {
        return NOTES.size();
    }

    public Note getNote(int id) {
        return NOTES.get(id);
    }

    public List<Note> getNoteList() {
        return NOTES;
    }

    public void putNewNote(String title, String text) {
        Note newNote = new Note(title, text);
        NOTES.add(newNote);
    }

}
