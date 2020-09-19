package com.example.systemize;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CalendarFragment extends ListFragment {
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<TaskItem> taskList;
    private String date;
    private LocalDate sunday;
    private LocalDate saturday;
    private TextView monthText;
    private Month month;
    private int year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        monthText = view.findViewById(R.id.month);
        setUpDate();
        buildRecyclerView(view);
        return view;
    }

    private void setUpDate(){
        LocalDate today = LocalDate.now();
        sunday  = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY){
            sunday = sunday.minusDays(1);
        }
        saturday = today;
        while (saturday.getDayOfWeek() != DayOfWeek.SATURDAY){
            saturday = saturday.plusDays(1);
        }
        month = LocalDate.now().getMonth();
        year = LocalDate.now().getYear();

        monthText.setText(month + " " + year);
    }


    private void buildRecyclerView(View view){
        recyclerView = view.findViewById(R.id.upcoming_tasks);
        readDatabase();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new ListAdapter(taskList, getResources().getFont(R.font.futura_medium), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                taskList.get(position).changeCompleted();
                System.out.println(taskList.get(position).getTitle());
                System.out.println(taskList.get(position).getCompleted());
            }
        });
    }

    private void readDatabase(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, LocalDate.now().getYear());
        c.set(Calendar.MONTH, LocalDate.now().getMonthValue());
        c.set(Calendar.DAY_OF_MONTH, LocalDate.now().getDayOfMonth());
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

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
            taskList.add(new TaskItem(id, title, category, completed));
        }
        cursor.close();

    }

    @Override
    public void saveCompletion(TaskItem taskItem) {
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

}
