package com.example.systemize;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.FontsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends ListFragment {
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<TaskItem> taskList;
    private TextView noTasks;
    private TextView yourTasks;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        noTasks = view.findViewById(R.id.no_tasks);
        yourTasks = view.findViewById(R.id.your_tasks);
        buildRecyclerView(view);

        return view;
    }

    public void saveCompletion(TaskItem taskItem){
        TaskHelper helper = new TaskHelper(Objects.requireNonNull(getActivity()).getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, String.valueOf(taskItem.getCompleted()));
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskItem.getID())};
        int count = db.update(TaskContract.TaskEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        helper.close();
    }

    private void testCompletion(int id){
        SQLiteDatabase db = new TaskHelper(Objects.requireNonNull(getActivity()).
                getApplicationContext()).getReadableDatabase();
        String sortOrder =
                BaseColumns._ID + " DESC";

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        cursor.moveToFirst();
        cursor.close();
    }
    private void buildRecyclerView(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        readDatabase();
        if (taskList.isEmpty()){
            noTasks.setVisibility(View.VISIBLE);
            yourTasks.setVisibility(View.INVISIBLE);
        }
        else {
            noTasks.setVisibility(View.INVISIBLE);
            yourTasks.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            listAdapter = new ListAdapter(taskList, getResources().getFont(R.font.futura_medium), this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(listAdapter);

            listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    taskList.get(position).changeCompleted();
                    saveCompletion(taskList.get(position));
                    testCompletion(taskList.get(position).getID());
                }
            });
        }
    }


    private void readDatabase(){
        String date = LocalDate.now().toString();
        SQLiteDatabase db = new TaskHelper(Objects.requireNonNull(getActivity()).
                getApplicationContext()).getReadableDatabase();
        String sortOrder =
                BaseColumns._ID + " DESC";

        String selection = TaskContract.TaskEntry.COLUMN_NAME_DATE + " = ?";
        String[] selectionArgs = { date};
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        taskList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TASK));
            String category = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_CATEGORY));
            boolean completed = Boolean.parseBoolean(cursor.getString(
                    cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)));
            String duration = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DURATION));
            String tempDate = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DATE));
            taskList.add(new TaskItem(id, title, category, completed, tempDate, duration));
        }
        cursor.close();
        db.close();

    }

}
