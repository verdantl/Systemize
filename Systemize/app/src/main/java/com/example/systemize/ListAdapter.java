package com.example.systemize;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<TaskItem> itemList;
    private Typeface typeface;
    private OnItemClickListener listener;
    private ListFragment fragment;


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
        public ListFragment fragment;
        public TaskItem taskItem;
        public CardView itemView;

        public ListViewHolder(@NonNull View itemView, final OnItemClickListener innerListener) {
            super(itemView);
            this.itemView = (CardView) itemView;
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
                            fragment.saveCompletion(taskItem);
                        }
                    }
                }
            });
        }
    }

    public ListAdapter(ArrayList<TaskItem> itemList, Typeface typeface, ListFragment fragment){
        this.itemList = itemList;
        this.typeface = typeface;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new ListViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        final TaskItem taskItem = itemList.get(position);
        holder.textView.setText(taskItem.getTitle());
        holder.textView.setTypeface(typeface);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fragment.showFragment(taskItem);
            }
        });
        holder.category.setImageResource(taskItem.getCategoryImage());
        holder.checkBox.setChecked(taskItem.getCompleted());
        holder.fragment = fragment;
        holder.taskItem = taskItem;
        holder.itemView.setCardBackgroundColor(taskItem.getColor());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
