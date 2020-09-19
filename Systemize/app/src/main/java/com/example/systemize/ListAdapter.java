package com.example.systemize;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<TaskItem> itemList;
    private Typeface typeface;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public static class ListViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        public TextView textView;
        public ImageView category;

        public ListViewHolder(@NonNull View itemView, final OnItemClickListener innerListener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.completed);
            textView = itemView.findViewById(R.id.task_name);
            category = itemView.findViewById(R.id.category);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (innerListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            innerListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ListAdapter(ArrayList<TaskItem> itemList, Typeface typeface){
        this.itemList = itemList;
        this.typeface = typeface;

    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new ListViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        TaskItem taskItem = itemList.get(position);
        holder.textView.setText(taskItem.getTitle());
        holder.textView.setTypeface(typeface);
        holder.category.setImageResource(taskItem.getCategoryImage());
        holder.checkBox.setChecked(taskItem.getCompleted());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
