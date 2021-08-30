package ru.geekbrains.notes.ui

import android.content.Context
import android.widget.TextView
import ru.geekbrains.notes.repository.LocalNotesRepository
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import ru.geekbrains.notes.*
import java.text.SimpleDateFormat
import java.util.*

class EditNoteFragment : Fragment() {
    private var note: Note? = null
    private lateinit var titleView: TextView
    private lateinit var textView: TextView
    private var colorSelected = Note.COLOR_WHITE
    private lateinit var localRepository: LocalNotesRepository
    private lateinit var navigation: Navigation
    private var publisher: Publisher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleView = view.findViewById(R.id.editText_title)
        textView = view.findViewById(R.id.editText_text)
        val dateView = view.findViewById<TextView>(R.id.textView_date)
        if (arguments != null) {
            note = requireArguments().getParcelable(ARG_NOTE)
            titleView.text = note?.title
            textView.text = note?.text
            colorSelected = note?.color ?: 0
            dateView.text = SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(note?.date)
        } else {
            dateView.text = SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(Date())
        }

        initColors(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mainActivity = context as MainActivity
        navigation = mainActivity.navigation
        publisher = mainActivity.publisher
        localRepository = mainActivity.localRepository
    }

    override fun onDetach() {
        super.onDetach()
        if (arguments != null) {
            note?.title = titleView.text.toString()
            note?.text = textView.text.toString()
            note?.color = colorSelected
            note?.date = Date()
            note?.let { localRepository.editNote(it) }
        } else {
            if (textView.text.isNotEmpty()) {
                val id = "id" + (localRepository.noteListSize + 1)
                val note =
                    Note(id, titleView.text.toString(), textView.text.toString(), colorSelected)
                localRepository.addNote(note)
            }
        }
        publisher = null
    }

    private fun initColors(view: View) {
        val currentColor = view.findViewById<ImageView>(R.id.edit_color_current)
        if (note != null) {
            currentColor.setImageResource(
                ColorManager(resources).getColorIdFromResourcesArray(
                    note!!.color
                )
            )
        } else currentColor.setImageResource(
            ColorManager(resources).getColorIdFromResourcesArray(
                Note.COLOR_WHITE
            )
        )
        currentColor.setOnClickListener { v: View? ->
            currentColor.visibility = View.GONE
            view.findViewById<View>(R.id.colors_container).visibility = View.VISIBLE
        }
        val colors: MutableList<ImageView> = ArrayList()
        colors.add(view.findViewById(R.id.edit_color_white))
        colors.add(view.findViewById(R.id.edit_color_green))
        colors.add(view.findViewById(R.id.edit_color_red))
        colors.add(view.findViewById(R.id.edit_color_blue))
        colors.add(view.findViewById(R.id.edit_color_yellow))
        colors.add(view.findViewById(R.id.edit_color_purple))
        for (i in colors.indices) {
            // ставим листнеры на вьюшки с кружочками
            colors[i].setOnClickListener { v: View? ->
                currentColor.visibility = View.VISIBLE
                colorSelected = i
                view.findViewById<View>(R.id.colors_container).visibility = View.GONE
                currentColor.setImageResource(
                    ColorManager(resources).getColorIdFromResourcesArray(
                        colorSelected
                    )
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(note: Note?): EditNoteFragment {
            val fragment = EditNoteFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_NOTE, note)
            fragment.arguments = bundle
            return fragment
        }

        private const val ARG_NOTE = "ARG_NOTE"
    }
}