package ru.geekbrains.notes.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import ru.geekbrains.notes.ui.EditNoteFragment.Companion.newInstance
import androidx.recyclerview.widget.LinearLayoutManager
import ru.geekbrains.notes.repository.LocalNotesRepository
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.os.Build
import android.view.ContextMenu.ContextMenuInfo
import android.view.*
import androidx.fragment.app.Fragment
import ru.geekbrains.notes.*

class NoteListFragment : Fragment() {
    private var email: String? = null
    private var onNoteClicked: OnNoteClicked? = null
    private var adapter: NoteListAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var localRepository: LocalNotesRepository? = null
    private var recyclerView: RecyclerView? = null
    private var publisher: Publisher? = null
    private var navigation: Navigation? = null
    private var mainActivity: MainActivity? = null

    interface OnNoteClicked {
        fun onNoteClicked(note: Note?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (arguments != null) {
            email = requireArguments().getString(ARG_EMAIL)
        }
        if (context is OnNoteClicked) {
            onNoteClicked = context
        }
        mainActivity = context as MainActivity
        mainActivity!!.initDrawer()
        publisher = mainActivity!!.publisher
        navigation = mainActivity!!.navigation
        mainActivity!!.initLocalRepository(email)
        localRepository = mainActivity!!.localRepository
    }

    // При создании фрагмента укажем его макет
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_list_fragment, container, false)
        val context = view.context
        initView(view, context)

        // Установим слушателя
        adapter!!.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemLongClick(v: View?, position: Int, itemView: View?) {
//                showPopupMenu(v, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView!!.showContextMenu(5f, 10f)
                }
            }

            override fun onItemClick(view: View?, position: Int) {
                showNoteDetails(localRepository!!.getNote(position))
            }
        })
        return view
    }

    override fun onResume() {
        super.onResume()

//        recyclerView.scrollToPosition(localRepository.getNoteListSize());
        mainActivity!!.throwRecyclerView(adapter, recyclerView)
        if (localRepository != null) {
            localRepository!!.syncList()
        }
    }

    override fun onDetach() {
        onNoteClicked = null
        publisher = null
        super.onDetach()
    }

    //    private void showPopupMenu(View view, int position) {
    //        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
    //        requireActivity().getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
    //
    //        popupMenu.setOnMenuItemClickListener(item -> {
    //            if (item.getItemId() == R.id.action_edit_note) {
    ////                localRepository.editNote(position);
    //                linearLayoutManager.scrollToPosition(repositoryManager.getNoteListSize());
    //                Toast.makeText(requireContext(), "Do something with " + repositoryManager.getNote(position).getTitle(), Toast.LENGTH_SHORT).show();
    //            }
    //            if (item.getItemId() == R.id.action_delete_note) {
    //                repositoryManager.removeNote(position);
    //                adapter.notifyItemRemoved(position);
    //            }
    //            if (item.getItemId() == R.id.action_clear) {
    //                repositoryManager.clear();
    //                adapter.notifyDataSetChanged();
    //            }
    //            return true;
    //        });
    //        popupMenu.show();
    //    }
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.popup_menu, menu)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = adapter!!.menuPosition
        if (item.itemId == R.id.action_edit_note) {
            navigation!!.addFragment(newInstance(localRepository!!.getNote(position)))


//            publisher.subscribe(new Observer() {
//                @Override
//                public void updateNoteData(Note note) {
//                int position = 0;
//
//                    for (int i = 0; i < repositoryManager.getNoteListSize(); i++) {
//                        if (repositoryManager.getNote(i).equals(note)) {
//                            position = i;
//                        }
//                    }
//
//                    repositoryManager.editNote(note);
//                    adapter.notifyItemChanged(position);
//                    recyclerView.smoothScrollToPosition(repositoryManager.getNoteListSize() - 1);
//                }
//            });
            linearLayoutManager!!.scrollToPosition(localRepository!!.noteListSize)
        }
        if (item.itemId == R.id.action_delete_note) {
            localRepository?.removeNote(adapter!!.menuPosition)
            //            adapter.notifyItemRemoved(adapter.getMenuPosition());
        }
        if (item.itemId == R.id.action_clear) {
            showAlertDialog()
        }
        return super.onContextItemSelected(item)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle(R.string.alert_dialog)
            .setMessage(R.string.alert_message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(R.string.alert_button_positive) { dialog, which -> localRepository!!.clear() }
            .setNegativeButton(R.string.alert_button_negative) { dialog, which -> }.show()
    }

    private fun showNoteDetails(note: Note) {
        if (onNoteClicked != null) {
            onNoteClicked!!.onNoteClicked(note)
        }
    }

    private fun initView(view: View, context: Context) {
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager?.stackFromEnd = true
        linearLayoutManager?.reverseLayout = true
        adapter = NoteListAdapter(this)
        adapter?.setResources(resources)
        adapter?.notifyDataSetChanged()
        recyclerView = view as RecyclerView
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.adapter = adapter
        mainActivity = context as MainActivity
    }

    companion object {
        private const val ARG_EMAIL = "ARG_EMAIL"
        private const val ARG_USERNAME = "ARG_USERNAME"
        @JvmStatic
        fun newInstance(email: String?): NoteListFragment {
            val fragment = NoteListFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EMAIL, email)
            fragment.arguments = bundle
            return fragment
        }
    }
}