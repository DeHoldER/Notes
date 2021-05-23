package ru.geekbrains.notes;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.notes.repository.LocalNotesRepository;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private Resources resources;

    private OnItemClickListener itemClickListener; // Слушатель будет устанавливаться извне

    LocalNotesRepository localRepository;

    private final Fragment fragment;

    private int menuPosition;

    public int getMenuPosition() {
        return menuPosition;
    }

    public NoteListAdapter(Fragment fragment) {
        this.fragment = fragment;
        MainActivity mainActivity = (MainActivity)fragment.getActivity();
        if (mainActivity != null) {
            localRepository = mainActivity.getLocalRepository();
        }
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(localRepository.getNote(position));
    }

    @Override
    public int getItemCount() {
        return localRepository.getNoteListSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView textPreview;
        private final ImageView color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_note_title);
            textPreview = itemView.findViewById(R.id.item_note_text_preview);
            color = itemView.findViewById(R.id.item_note_color);

            registerContextMenu(itemView);

            // Обработчик нажатий на этом ViewHolder
            itemView.setOnLongClickListener(v -> {
                if (itemClickListener != null) {
                    menuPosition = getLayoutPosition();
                    itemClickListener.onItemLongClick(v, getAdapterPosition(), itemView);
                }
                return true;
            });
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void bind(Note note) {
            ColorManager colorManager = new ColorManager(resources);
            title.setText(note.getTitle());
            textPreview.setText(note.getText());
            color.setImageResource(colorManager.getColorIdFromResourcesArray(note.getColor()));
        }

    }

    // Сеттер слушателя нажатий
    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View v, int adapterPosition, View itemView);

    }
}
