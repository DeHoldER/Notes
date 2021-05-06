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

1. Подумайте о функционале вашего приложения заметок. Какие экраны там могут быть, помимо
основного со списком заметок? Как можно использовать меню и всплывающее меню в вашем
приложении? Не обязательно сразу пытаться реализовать весь этот функционал, достаточно
создать макеты и структуру, а реализацию пока заменить на заглушки или всплывающие
уведомления (Toast). Используйте подход Single Activity для отображения экранов.

В качестве примера: на главном экране приложения у вас список всех заметок, при нажатии
на заметку открывается экран с этой заметкой. В меню главного экрана у вас есть иконка
поиска по заметкам и сортировка. В меню «Заметки» у вас есть иконки «Переслать» (или
«Поделиться»), «Добавить ссылку или фотографию к заметке».

2. Создайте боковое навигационное меню для своего приложения и добавьте туда хотя бы один
экран, например «Настройки» или «О приложении».

3. * Создайте полноценный заголовок для NavigationDrawer’а. К примеру, аватарка пользователя,
его имя и какая-то дополнительная информация.

4. * Разберитесь, как можно сделать, и сделайте корректировку даты создания при помощи
DatePicker.

 */

