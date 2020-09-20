package com.example.systemize;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Objects;

public class CalendarFragment extends ListFragment {
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<TaskItem> taskList;
    private String date;
    private LocalDate sunday;
    private LocalDate nextSunday;
    private TextView monthText;
    private Month month;
    private int year;
    private View calendarView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        monthText = view.findViewById(R.id.month);
        calendarView = view;
        setUpDate();
        setUpArrows();
        buildRecyclerView(view);
        setCalendar(view);
        return view;
    }

    private void setUpArrows(){
        ImageButton left = calendarView.findViewById(R.id.left_arrow);
        ImageButton right = calendarView.findViewById(R.id.right_arrow);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveLeft();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRight();
            }
        });
    }

    private void moveLeft(){
        System.out.println("We moving left");
    }

    private void moveRight(){

    }

    private void setCalendar(View view){
        int[] days = {R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday,
                R.id.friday, R.id.saturday};
        int i = 0;
        LocalDate temp = sunday;
        while (i < 7){
            TextView dayName = view.findViewById(days[i]).findViewById(R.id.day_of_week);
            String text = String.valueOf(temp.getDayOfWeek().name().charAt(0));
            dayName.setText(text);
            TextView numDay = view.findViewById(days[i]).findViewById(R.id.date_num);
            numDay.setText(String.valueOf(temp.getDayOfMonth()));
            i++;
            temp = temp.plusDays(1);
        }
    }

    private void setUpDate(){
        LocalDate today = LocalDate.now();
        sunday  = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY){
            sunday = sunday.minusDays(1);
        }
        nextSunday = today;
        nextSunday = nextSunday.plusDays(1);
        while (nextSunday.getDayOfWeek() != DayOfWeek.SUNDAY){
            nextSunday = nextSunday.plusDays(1);
        }
        month = LocalDate.now().getMonth();
        year = LocalDate.now().getYear();
        String monthString = month.name().substring(0,1).toUpperCase() +
                month.name().substring(1).toLowerCase();
        monthText.setText(monthString + " " + year);
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
            }
        });
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
