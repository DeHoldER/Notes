package ru.geekbrains.notes

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.notes.repository.LocalNotesRepository
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment

class NoteListAdapter(fragment: Fragment) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private var resources: Resources? = null
    private var itemClickListener // Слушатель будет устанавливаться извне
            : OnItemClickListener? = null
    var localRepository: LocalNotesRepository? = null
    private val fragment: Fragment?
    var menuPosition = 0
        private set

    fun setResources(resources: Resources?) {
        this.resources = resources
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(localRepository!!.getNote(position))
    }

    override fun getItemCount(): Int {
        return localRepository!!.noteListSize
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_note_title)
        private val textPreview: TextView = itemView.findViewById(R.id.item_note_text_preview)
        private val color: ImageView = itemView.findViewById(R.id.item_note_color)
        private fun registerContextMenu(itemView: View) {
            if (fragment != null) {
                itemView.setOnLongClickListener { v: View? ->
                    menuPosition = layoutPosition
                    false
                }
                fragment.registerForContextMenu(itemView)
            }
        }

        fun bind(note: Note) {
            val colorManager = ColorManager(resources!!)
            title.text = note.title
            textPreview.text = note.text
            color.setImageResource(colorManager.getColorIdFromResourcesArray(note.color))
        }

        init {
            registerContextMenu(itemView)

            // Обработчик нажатий на этом ViewHolder
            itemView.setOnLongClickListener { v: View? ->
                if (itemClickListener != null) {
                    menuPosition = layoutPosition
                    itemClickListener!!.onItemLongClick(v, adapterPosition, itemView)
                }
                true
            }
            itemView.setOnClickListener { v: View? ->
                if (itemClickListener != null) {
                    itemClickListener!!.onItemClick(v, adapterPosition)
                }
            }
        }
    }

    // Сеттер слушателя нажатий
    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    // Интерфейс для обработки нажатий
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(v: View?, adapterPosition: Int, itemView: View?)
    }

    init {
        this.fragment = fragment
        val mainActivity = fragment.activity as MainActivity?
        if (mainActivity != null) {
            localRepository = mainActivity.localRepository
        }
    }
}