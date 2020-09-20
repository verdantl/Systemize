package com.example.systemize;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserFragment extends Fragment {
    private HorizontalBarChart barChart;
    private String[] dates;
    private String dateString;
    private HashMap<String, ArrayList<TaskItem>> data; //Date to list of tasks on that date
    private final String[] categories = {"Work", "Personal", "Social", "Finances", "Family", "School", "Other"};
    private int[] colorArray = {Color.BLACK, Color.BLUE, Color.YELLOW, Color.GRAY, Color.GREEN, Color.CYAN, Color.MAGENTA};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        barChart = view.findViewById(R.id.bargraph);
        setUpDates();
        for (String date: dates){
            dateString = date;
            readDatabase();
        }
        setUpGraph();

        test();
        return view;
    }

    private void setUpDates(){
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        dates = new String[7];
        data = new HashMap<>();
        for (int i = 0; i < 7; i++){
            dates[i] = today.minusDays(i).toString();
            data.put(today.minusDays(i).getDayOfWeek().toString(), new ArrayList<TaskItem>());
        }
    }

    private void test(){
        for (String string: dates){
            System.out.println(string);
        }
        for (String string: data.keySet()){
            System.out.println(string);
        }
    }

    private HashMap<String, Integer> getCategoryMap(String date){
        HashMap<String, Integer> categoryMap = new HashMap<>();
        for (String category: categories){
            categoryMap.put(category, 0);
        }
        ArrayList<TaskItem> entries = data.get(date);
        if (entries != null) {


            for (TaskItem item : entries) {
                if (item.getCompleted()) {
                    Integer old = categoryMap.get(item.getCategory());
                    categoryMap.replace(item.getCategory(), old + 1);
                }
            }
        }
        return categoryMap;
    }

    private ArrayList<BarEntry> dataValues(){
        ArrayList<BarEntry> values = new ArrayList<>();
//        values.add(new BarEntry(0, new float[]{0, 3, 5}));
//        values.add(new BarEntry(1, new float[]{1, 5, 2}));
//        values.add(new BarEntry(2, new float[]{, 3, 5}));
        int max = 0;
        for (int i=0; i < dates.length ; i++){
            float[] tasksPerCategory = new float[7]; //data for all 7 categories on a given day
            for (int j = 0; j < categories.length; j++){
                int numTasksCompleted = getCategoryMap(dates[i]).get(categories[j]); //This is for the given category on a day
                if (numTasksCompleted > max){
                    max = numTasksCompleted;
                }
                tasksPerCategory[j] = numTasksCompleted;
                values.add(new BarEntry(i, tasksPerCategory));
            }
        }
        return values;
    }

    private void setUpGraph(){
        BarDataSet barDataSet = new BarDataSet(dataValues(), null);
        barDataSet.setColors(colorArray);
        BarData barData = new BarData((barDataSet));
        barChart.setData(barData);
    }

    private void readDatabase(){
        SQLiteDatabase db = new TaskHelper(Objects.requireNonNull(getActivity()).
                getApplicationContext()).getReadableDatabase();
        String sortOrder =
                TaskContract.TaskEntry.COLUMN_NAME_DATE + " DESC";
        String selection = TaskContract.TaskEntry.COLUMN_NAME_DATE + " = ?";
        String[] selectionArgs = {dateString};
        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        while (cursor.moveToNext()){
            LocalDate tempDate = LocalDate.parse(cursor.getString(cursor.getColumnIndex(
                    TaskContract.TaskEntry.COLUMN_NAME_DATE)));
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TASK));
            String category = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_CATEGORY));
            boolean completed = Boolean.parseBoolean(cursor.getString(
                    cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED)));
            Objects.requireNonNull(data.get(tempDate.getDayOfWeek().toString())).add(
                    new TaskItem(id, title, category, completed));
        }
        cursor.close();
    }

}
