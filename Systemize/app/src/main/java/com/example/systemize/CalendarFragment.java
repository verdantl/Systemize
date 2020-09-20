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
    private LocalDate firstSunday;
    private LocalDate nextSunday;
    private Month month;
    private int year;
    private View calendarView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view;
        date = LocalDate.now().toString();
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
        left.setBackgroundColor(0);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRight();
            }
        });
        right.setBackgroundColor(0);
    }

    private void moveLeft(){
        sunday = sunday.minusDays(7);
        nextSunday = nextSunday.minusDays(7);
        if (sunday.equals(firstSunday)) {
            date = LocalDate.now().toString();
        }
        else{
            date = sunday.toString();
        }
        setCalendar(calendarView);
        buildRecyclerView(calendarView);
    }

    private void moveRight(){
        sunday = sunday.plusDays(7);
        nextSunday = nextSunday.plusDays(7);
        if (sunday.equals(firstSunday)) {
            date = LocalDate.now().toString();
        }
        else{
            date = sunday.toString();
        }
        setCalendar(calendarView);
        buildRecyclerView(calendarView);
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
            view.findViewById(days[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetClickedColors();
                    seeUpcomingTasks(view);
                }
            });
            i++;
            temp = temp.plusDays(1);
        }
        resetClickedColors();
        month = sunday.getMonth();
        String monthString = month.name().substring(0,1).toUpperCase() +
                month.name().substring(1).toLowerCase();
        TextView monthText = view.findViewById(R.id.month);
        monthText.setText(monthString + " " + year);
    }

    private void resetClickedColors(){
        int[] days = {R.id.sunday, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday,
                R.id.friday, R.id.saturday};
        for (int i = 0; i < days.length; i++){
            calendarView.findViewById(days[i]).setBackgroundColor(0);
        }
    }

    private void seeUpcomingTasks(View view){
        taskList = new ArrayList<>();
        view.setBackgroundColor(getResources().getColor(R.color.yellow));
        TextView textView = view.findViewById(R.id.date_num);
        String dateNum = textView.getText().toString();
        LocalDate tempNew = sunday;
        while (!String.valueOf(tempNew.getDayOfMonth()).equals(dateNum)){

            tempNew = tempNew.plusDays(1);
        }
        date = tempNew.toString();
        buildRecyclerView(calendarView);
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
        firstSunday = sunday;
        setCalendar(calendarView);
    }


    private void buildRecyclerView(View view){
        recyclerView = view.findViewById(R.id.upcoming_tasks);
        readWeeklyDatabase(date);
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

    private void readWeeklyDatabase(String startDay){
        taskList = new ArrayList<>();
        LocalDate temp = LocalDate.parse(startDay);
        while (!temp.equals(nextSunday)){
            readDatabase(temp.toString());
            temp = temp.plusDays(1);

        }
    }
    private void readDatabase(String date){
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
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TASK));
            String category = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_CATEGORY));
            boolean completed = Boolean.parseBoolean(cursor.getString(
                    cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)));
            taskList.add(new TaskItem(id, title, category, completed));
        }
        cursor.close();
        db.close();

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
