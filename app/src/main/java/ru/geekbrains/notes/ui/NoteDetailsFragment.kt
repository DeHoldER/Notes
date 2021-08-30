package ru.geekbrains.notes.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.geekbrains.notes.*
import java.text.SimpleDateFormat

class NoteDetailsFragment : Fragment() {
    private var navigation: Navigation? = null
    var note: Note? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as MainActivity
        navigation = activity.navigation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        MaterialButton button = view.findViewById(R.id.button_edit_note);
        val idView = view.findViewById<TextView>(R.id.textView_id)
        val titleView = view.findViewById<TextView>(R.id.textView_title)
        val dateView = view.findViewById<TextView>(R.id.textView_date)
        val textView = view.findViewById<TextView>(R.id.textView_text)
        val color = view.findViewById<ImageView>(R.id.note_details_color)

//        button.setOnClickListener(v -> {
//            navigation.addFragment(EditNoteFragment.newInstance(note));
//        });
        titleView.setOnClickListener { v: View? ->
            navigation?.addFragment(
                EditNoteFragment.newInstance(
                    note
                )
            )
        }
        textView.setOnClickListener { v: View? ->
            navigation?.addFragment(
                EditNoteFragment.newInstance(
                    note
                )
            )
        }
        color.setOnClickListener { v: View? ->
            navigation?.addFragment(
                EditNoteFragment.newInstance(
                    note
                )
            )
        }
        if (arguments != null) {
            note = requireArguments().getParcelable(ARG_NOTE)
        }
        if (note != null) {
            color.setImageResource(ColorManager(resources).getColorIdFromResourcesArray(note!!.color))
            titleView.text = note?.title
            idView.text = note?.id
            textView.text = note?.text
            dateView.text = SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(note!!.date)
        }
    }

    companion object {
        private const val ARG_NOTE = "ARG_NOTE"
        fun newInstance(note: Note?): NoteDetailsFragment {
            val fragment = NoteDetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_NOTE, note)
            fragment.arguments = bundle
            return fragment
        }
    }
}