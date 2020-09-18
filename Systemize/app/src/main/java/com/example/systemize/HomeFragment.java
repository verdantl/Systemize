package com.example.systemize;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter listAdapter;
    private ArrayList<TaskItem> taskList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        taskList.add(new TaskItem("Title here", R.drawable.ic_baseline_home_24));

        taskList.add(new TaskItem("Title2 here", R.drawable.ic_baseline_home_24));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new ListAdapter(taskList, getResources().getFont(R.font.futura_medium));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        return view;
    }

    private void readDatabase(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, LocalDate.now().getYear());
        c.set(Calendar.MONTH, LocalDate.now().getMonthValue());
        c.set(Calendar.DAY_OF_MONTH, LocalDate.now().getDayOfMonth());
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()).toString();

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
//        while (cursor.moveToNext()){
//            taskList.add(new TaskItem())
//        }

    }

    private void updateRecycler(){
    }

}
