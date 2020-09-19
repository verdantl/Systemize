package com.example.systemize;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class UserFragment extends Fragment {
    private HorizontalBarChart horizontalBarChart;
    private ArrayList<LocalDate> dates;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        horizontalBarChart = view.findViewById(R.id.bargraph);
        setUpDates();
        setUpGraph();
        return view;
    }

    private void setUpDates(){
        LocalDate today = LocalDate.now();
        LocalDate oneWeekBefore = today.minusDays(6);
        while (oneWeekBefore != today){
            dates.add(oneWeekBefore);
            oneWeekBefore = oneWeekBefore.plusDays(1);
        }
        dates.add(today);
    }

    private void setUpGraph(){

    }

    private void readDatabase(){
        SQLiteDatabase db = new WeeklyHelper(Objects.requireNonNull(getActivity()).
                getApplicationContext()).getReadableDatabase();
        String sortOrder =
                BaseColumns._ID + " DESC";
        String selection = LocalDate.parse(TaskContract.TaskEntry.COLUMN_NAME_DATE) + " < ?";
        String[] selectionArgs = { LocalDate.now().toString()};
    }

}
