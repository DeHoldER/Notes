package ru.geekbrains.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements NoteListFragment.OnNoteClicked {

    private boolean isLandscape;
    private FragmentManager fragmentManager;

    private Note lastOpenedNote;
    private static final String KEY_LAST_NOTE = "KEY_LAST_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            lastOpenedNote = savedInstanceState.getParcelable(KEY_LAST_NOTE);
        }

        fragmentManager = getSupportFragmentManager();

        isLandscape = getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;

        Fragment fragmentContainer = fragmentManager.findFragmentById(R.id.fragment_container);

        if (!isLandscape) {
            if (lastOpenedNote != null) {
                if (fragmentContainer instanceof NoteDetailsFragment) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new NoteListFragment())
                            .commit();
                }
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_container, NoteDetailsFragment.newInstance(lastOpenedNote))
                        .addToBackStack(null)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_container, new NoteListFragment())
                        .commit();
            }
        } else {
            if (lastOpenedNote != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.list_container, new NoteListFragment())
                        .replace(R.id.detail_container, NoteDetailsFragment.newInstance(lastOpenedNote))
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.list_container, new NoteListFragment())
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAST_NOTE, lastOpenedNote);
    }

    @Override
    public void onNoteClicked(Note note) {
        lastOpenedNote = note;

        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }

        NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(note);
        if (!isLandscape) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.list_container, new NoteListFragment())
                    .replace(R.id.detail_container, detailsFragment)
                    .commit();
        }
    }
}


/*

1. Создайте класс данных со структурой заметок: название заметки, описание заметки, дата
создания и т. п.
2. Создайте фрагмент для вывода этих данных.
3. Встройте этот фрагмент в активити. У вас должен получиться экран с заметками, который мы
будем улучшать с каждым новым уроком.
4. Добавьте фрагмент, в котором открывается заметка. По аналогии с примером из урока: если
нажать на элемент списка в портретной ориентации — открывается новое окно, если нажать в
ландшафтной — окно открывается рядом.
5. * Разберитесь, как можно сделать, и сделайте корректировку даты создания при помощи
DatePicker.

 */

