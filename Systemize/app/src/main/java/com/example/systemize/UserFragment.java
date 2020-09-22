package com.example.systemize;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserFragment extends Fragment {
    private HorizontalBarChart barChart;
    private String[] dates;
    private String dateString;
    private String[] datesOfWeek;
    private HashMap<String, ArrayList<TaskItem>> data; //Date to list of tasks on that date
    private final String[] categories = {"Work", "Personal", "Social", "Finances", "Family", "School", "Other"};
    private int[] colorArray;

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
        colorArray = new int[]{getResources().getColor(R.color.work_colour),
                getResources().getColor(R.color.personal_colour),
                getResources().getColor(R.color.social_colour),
                getResources().getColor(R.color.finances_colour),
                getResources().getColor(R.color.family_colour),
                getResources().getColor(R.color.school_colour),
                R.color.olive};
        setUpGraph();
        return view;
    }

    private void setUpDates(){
        LocalDate today = LocalDate.now();
        dates = new String[7];
        data = new HashMap<>();
        datesOfWeek = new String[7];
        for (int i = 0; i < 7; i++){
            dates[i] = today.minusDays(i).toString();
            data.put(today.minusDays(i).toString(), new ArrayList<TaskItem>());
            datesOfWeek[i] = today.minusDays(i).getDayOfWeek().toString();
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
        int max = 0;
        for (int i=0; i < dates.length ; i++){
            float[] tasksPerCategory = new float[7]; //data for all 7 categories on a given day
            for (int j = 0; j < categories.length; j++){
                int numTasksCompleted = getCategoryMap(dates[i]).get(categories[j]);//This is for the given category on a day
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
        barDataSet.setStackLabels(categories);
        barDataSet.setDrawIcons(true);
        barDataSet.setColors(colorArray);
        barDataSet.setDrawValues(false);
        BarData barData = new BarData((barDataSet));
        barData.setValueFormatter(new MyYAxisValueFormatter());
        barChart.setData(barData);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(datesOfWeek));
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.setDrawBarShadow(false);
        barChart.setPinchZoom(false);
        XAxis xAxis = barChart.getXAxis();
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        barChart.animateY(1000);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.invalidate();
    }

    public class MyYAxisValueFormatter extends ValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter(){
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
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
            String duration = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DURATION));
            String temporaryDate = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DATE));
            Objects.requireNonNull(data.get(tempDate.toString())).add(
                    new TaskItem(id, title, category, completed, temporaryDate, duration));
        }
        cursor.close();
    }

}
