package ru.geekbrains.notes

import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.notes.ui.NoteListFragment.OnNoteClicked
import ru.geekbrains.notes.ui.NoteListFragment
import ru.geekbrains.notes.repository.LocalNotesRepository
import android.os.Bundle
import android.content.res.Configuration
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import ru.geekbrains.notes.ui.EditNoteFragment
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.geekbrains.notes.ui.NoteDetailsFragment
import ru.geekbrains.notes.ui.AboutFragment
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnNoteClicked {
    private var isLandscape = false
    private var fragmentManager: FragmentManager? = null
    private var lastOpenedNote: Note? = null
    private val noteListFragment: NoteListFragment? = null
    private val fragmentContainer: Fragment? = null
    lateinit var navigation: Navigation
    lateinit var localRepository: LocalNotesRepository
    val publisher = Publisher()
    private var userName: String? = null
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation = Navigation(supportFragmentManager, resources)
        initFields(savedInstanceState)

// раскомментировать для авторизации через gmail

//        if (isAuthorized(savedInstanceState)) {
//            navigation.addFragment(NoteListFragment.newInstance(userEmail), false);
//        } else navigation.addFragment(AuthFragment.newInstance(), false);
        navigation.addFragment(NoteListFragment.newInstance("testEmail"), false)
    }

    fun isAuthorized(savedInstanceState: Bundle?): Boolean {
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        return if (savedInstanceState == null) {
            userEmail = sharedPreferences.getString("userEmail", "empty")
            userName = sharedPreferences.getString("userName", "empty")
            userEmail != "empty"
        } else false
    }

    fun initLocalRepository(email: String?) {
        localRepository = LocalNotesRepository(email)
    }

    override fun onResume() {
        super.onResume()
        localRepository.syncList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_LAST_NOTE, lastOpenedNote)
    }

    private fun initFields(savedInstanceState: Bundle?) {
        lastOpenedNote = savedInstanceState?.getParcelable(KEY_LAST_NOTE)
        fragmentManager = supportFragmentManager
        isLandscape = resources
            .configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun initDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // Обработка навигационного меню
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            if (navigateFragment(id)) {
                drawer.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            }
            false
        }
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_new_note) {
                if (fragmentContainer !is EditNoteFragment) {
                    navigation.addFragment(EditNoteFragment(), true)
                    return@setOnMenuItemClickListener true
                }
            }
            if (item.itemId == R.id.action_sorting) {
                localRepository.syncList()
                Toast.makeText(this@MainActivity, "Синхронизация завершена", Toast.LENGTH_SHORT)
                    .show()
                return@setOnMenuItemClickListener true
            }
            if (item.itemId == R.id.action_search) {
                Toast.makeText(this@MainActivity, "Search clicked", Toast.LENGTH_SHORT).show()
                return@setOnMenuItemClickListener true
            }
            false
        }
        val headerView = navigationView.getHeaderView(0)
        val navUserName = headerView.findViewById<View>(R.id.user_name) as TextView
        val navUserEmail = headerView.findViewById<View>(R.id.user_account) as TextView
        navUserName.text = userName
        navUserEmail.text = userEmail
    }

    fun setUserOnMenu(userName: String?, userEmail: String?) {
        this.userName = userName
        this.userEmail = userEmail
    }

    private fun navigateFragment(id: Int): Boolean {
        val settingsPlugFragment =
            NoteDetailsFragment.newInstance(Note("", "Settings", "Заглушка для настроек"))
        when (id) {
            R.id.action_goto_note_list -> {
                navigation.addFragment(NoteListFragment.newInstance(userEmail), false)
                return true
            }
            R.id.action_settings -> {
                navigation.addFragment(settingsPlugFragment)
                return true
            }
            R.id.action_trash -> {
                Toast.makeText(this@MainActivity, "В разработке...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_generate_notes -> {
                localRepository.fillList(0)
                localRepository.syncList()
                return true
            }
            R.id.action_about -> {
                navigation.addFragment(AboutFragment())
                return true
            }
        }
        return false
    }

    override fun onNoteClicked(note: Note?) {
        lastOpenedNote = note
        val detailsFragment = NoteDetailsFragment.newInstance(note)
        navigation.addFragment(detailsFragment, true)
    }

    fun throwRecyclerView(adapter: NoteListAdapter?, recyclerView: RecyclerView?) {
        localRepository.setAdapter(adapter, recyclerView)
    }

    companion object {
        private const val KEY_LAST_NOTE = "KEY_LAST_NOTE"
    }
}