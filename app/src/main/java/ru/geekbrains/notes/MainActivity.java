package ru.geekbrains.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements NoteListFragment.OnNoteClicked {

    private boolean isLandscape;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        // Ставим флаг в зависимости от ориентации
        isLandscape = getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;

        Fragment currentPortraitFragment = fragmentManager
                .findFragmentById(R.id.fragment_container);
        Fragment currentLandscapeFragment = fragmentManager
                .findFragmentById(R.id.detail_container);

        if (!isLandscape) {

            if (currentPortraitFragment == null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new NoteListFragment())
                        .commit();
            }
        } else {

            if (currentLandscapeFragment != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.list_container, new NoteListFragment())
                        .replace(R.id.detail_container, currentLandscapeFragment)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.list_container, new NoteListFragment())
                        .commit();
            }
        }


//        if (isLandscape) {
//            Note emptyNote = new Note("Заглушка", "wflwef wefm wemf wep fmwepf p");
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.detail_container, NoteDetailsFragment.newInstance(emptyNote))
//                    .replace(R.id.fragment_container, new NoteListFragment())
//                    .commit();
//        }
    }

    @Override
    public void onNoteClicked(Note note) {
        Fragment detailsFragment = NoteDetailsFragment.newInstance(note);

        if (!isLandscape) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.list_container, new NoteListFragment())
                    .replace(R.id.detail_container, NoteDetailsFragment.newInstance(note))
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

