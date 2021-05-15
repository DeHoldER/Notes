package ru.geekbrains.notes.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.AboutFragment;
import ru.geekbrains.notes.domain.Note;
import ru.geekbrains.notes.NoteDetailsFragment;
import ru.geekbrains.notes.NoteListFragment;
import ru.geekbrains.notes.R;

public class MainActivity extends AppCompatActivity implements NoteListFragment.OnNoteClicked {

    private boolean isLandscape;
    private FragmentManager fragmentManager;

    private Note lastOpenedNote;
    private static final String KEY_LAST_NOTE = "KEY_LAST_NOTE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields(savedInstanceState);
        loadList();
        initDrawer();

//        RecyclerView noteList = findViewById(R.id.note_list_recycler);
//
//        NoteListAdapter adapter = new NoteListAdapter();
//        noteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        noteList.setAdapter(adapter);
//        adapter.addData(dataList);
//        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAST_NOTE, lastOpenedNote);
    }


    private void initFields(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            lastOpenedNote = savedInstanceState.getParcelable(KEY_LAST_NOTE);
        }

        fragmentManager = getSupportFragmentManager();

        isLandscape = getResources()
                .getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка навигационного меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (navigateFragment(id)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_settings) {
                    Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (item.getItemId() == R.id.action_sorting) {
                    Toast.makeText(MainActivity.this, "Sorting clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (item.getItemId() == R.id.action_search) {
                    Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

    }

    private boolean navigateFragment(int id) {
        NoteDetailsFragment settingsPlugFragment = NoteDetailsFragment.newInstance(new Note("id1", "Settings", "Заглушка для настроек"));
        switch (id) {
            case R.id.action_settings:
                addFragment(settingsPlugFragment);
                return true;
            case R.id.action_about:
                addFragment(new AboutFragment());
                return true;
        }
        return false;
    }

    private void loadList() {
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
    public void onNoteClicked(Note note) {
        lastOpenedNote = note;
        NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(note);
        addFragment(detailsFragment);
    }

    public void addFragment(Fragment fragment) {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }

        if (!isLandscape) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        }
    }
}



/*

0. Подумайте о функционале вашего приложения заметок. Какие экраны там могут быть, помимо
основного со списком заметок? Как можно использовать меню и всплывающее меню в вашем
приложении? Не обязательно сразу пытаться реализовать весь этот функционал, достаточно
создать макеты и структуру, а реализацию пока заменить на заглушки или всплывающие
уведомления (Toast). Используйте подход Single Activity для отображения экранов.

В качестве примера: на главном экране приложения у вас список всех заметок, при нажатии
на заметку открывается экран с этой заметкой. В меню главного экрана у вас есть иконка
поиска по заметкам и сортировка. В меню «Заметки» у вас есть иконки «Переслать» (или
«Поделиться»), «Добавить ссылку или фотографию к заметке».

1. Создайте список ваших заметок.
2. Создайте карточку для элемента списка.
3. Класс данных, созданный на шестом уроке, используйте для заполнения карточки списка.
4. * Создайте фрагмент для редактирования данных в конкретной карточке. Этот фрагмент пока
можно вызвать через основное меню.

4. * Разберитесь, как можно сделать, и сделайте корректировку даты создания при помощи
DatePicker.

 */
