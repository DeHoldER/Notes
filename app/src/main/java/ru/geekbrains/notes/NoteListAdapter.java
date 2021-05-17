package ru.geekbrains.notes;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.loader.ResourcesProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.repository.NotesRepositoryImpl;
import ru.geekbrains.notes.repository.RepositoryManager;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {

    private Resources resources;

    private OnItemClickListener itemClickListener; // Слушатель будет устанавливаться извне

    RepositoryManager repositoryManager = new NotesRepositoryImpl();

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.MyViewHolder holder, int position) {

        ColorManager colorManager = new ColorManager(resources);

        holder.title.setText(repositoryManager.getNote(position).getTitle());
        holder.textPreview.setText(repositoryManager.getNote(position).getText());
        holder.color.setImageResource(colorManager.getColorIdFromResourcesArray(repositoryManager.getNote(position).getColor()));

    }

    @Override
    public int getItemCount() {
        return repositoryManager.getNoteListSize();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView textPreview;
        ImageView color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_note_title);
            textPreview = itemView.findViewById(R.id.item_note_text_preview);
            color = itemView.findViewById(R.id.item_note_color);

            // Обработчик нажатий на этом ViewHolder
            itemView.setOnLongClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(v, getAdapterPosition());
                } return true;
            });
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    // Сеттер слушателя нажатий
    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View v, int adapterPosition);

    }
}
