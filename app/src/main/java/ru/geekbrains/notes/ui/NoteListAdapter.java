package ru.geekbrains.notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.R;
import ru.geekbrains.notes.domain.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {

    private OnItemClickListener itemClickListener; // Слушатель будет устанавливаться извне

    private final ArrayList<Note> data = new ArrayList<>();

    public void addData(List<Note> toAdd) {
        data.addAll(toAdd);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.MyViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_note);

            // Обработчик нажатий на этом ViewHolder
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    // Сеттер слушателя нажатий
    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий, как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
