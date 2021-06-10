package ru.geekbrains.notes;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import ru.geekbrains.notes.repository.LocalNotesRepository;
import ru.geekbrains.notes.ui.AboutFragment;
import ru.geekbrains.notes.ui.AuthFragment;
import ru.geekbrains.notes.ui.EditNoteFragment;
import ru.geekbrains.notes.ui.NoteDetailsFragment;
import ru.geekbrains.notes.ui.NoteListFragment;


public class MainActivity extends AppCompatActivity implements NoteListFragment.OnNoteClicked {

    private boolean isLandscape;
    private FragmentManager fragmentManager;

    private Note lastOpenedNote;
    private static final String KEY_LAST_NOTE = "KEY_LAST_NOTE";

    private NoteListFragment noteListFragment;
    private Fragment fragmentContainer;

    private Navigation navigation;
    private LocalNotesRepository localRepository;

    private Publisher publisher = new Publisher();

    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = new Navigation(getSupportFragmentManager(), getResources());


        initFields(savedInstanceState);

        if (isAuthorized(savedInstanceState)) {
            navigation.addFragment(NoteListFragment.newInstance(userEmail), false);
        } else navigation.addFragment(AuthFragment.newInstance(), false);

    }

    public boolean isAuthorized(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        if (savedInstanceState == null) {
            userEmail = sharedPreferences.getString("userEmail", "empty");
            userName = sharedPreferences.getString("userName", "empty");
            return !userEmail.equals("empty");
        } else return false;
    }

    public void initLocalRepository(String email) {
        localRepository = new LocalNotesRepository(email);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        if (localRepository != null) {
            localRepository.syncList();
        }
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

    public void initDrawer() {
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
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (navigateFragment(id)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_new_note) {
                if (!(fragmentContainer instanceof EditNoteFragment)) {
                    navigation.addFragment(new EditNoteFragment(), true);

                    return true;
                }
            }

            if (item.getItemId() == R.id.action_sorting) {
                localRepository.syncList();
                Toast.makeText(MainActivity.this, "Синхронизация завершена", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (item.getItemId() == R.id.action_search) {
                Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = (TextView) headerView.findViewById(R.id.user_name);
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.user_account);

        navUserName.setText(userName);
        navUserEmail.setText(userEmail);

    }

    public void setUserOnMenu(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    private boolean navigateFragment(int id) {
        NoteDetailsFragment settingsPlugFragment = NoteDetailsFragment.newInstance(new Note("", "Settings", "Заглушка для настроек"));
        switch (id) {
            case R.id.action_goto_note_list:
                navigation.addFragment(NoteListFragment.newInstance(userEmail), false);
                return true;
            case R.id.action_settings:
                navigation.addFragment(settingsPlugFragment);
                return true;
            case R.id.action_trash:
                Toast.makeText(MainActivity.this, "В разработке...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_generate_notes:
                localRepository.fillList(0);
                localRepository.syncList();
                return true;
            case R.id.action_about:
                navigation.addFragment(new AboutFragment());
                return true;
        }
        return false;
    }

    @Override
    public void onNoteClicked(Note note) {
        lastOpenedNote = note;
        NoteDetailsFragment detailsFragment = NoteDetailsFragment.newInstance(note);
        navigation.addFragment(detailsFragment, true);
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public LocalNotesRepository getLocalRepository() {
        return localRepository;
    }

    public void throwRecyclerView(NoteListAdapter adapter, RecyclerView recyclerView) {
        localRepository.setAdapter(adapter, recyclerView);
    }

}


/*
Подумайте о функционале вашего приложения заметок.
В качестве примера: на главном экране приложения у вас список всех заметок, при нажатии
на заметку открывается экран с этой заметкой. В меню главного экрана у вас есть иконка
поиска по заметкам и сортировка. В меню «Заметки» у вас есть иконки «Переслать» (или
«Поделиться»), «Добавить ссылку или фотографию к заметке».

1. Создайте список ваших заметок.
2. Создайте карточку для элемента списка.
4. * Разберитесь, как можно сделать, и сделайте корректировку даты создания при помощи
DatePicker.
 */

