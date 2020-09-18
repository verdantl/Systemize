package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        String[] categories = {"Work", "Personal", "Social", "Finances", "Family", "School"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner,
                new ArrayList<>(Arrays.asList(categories)));
        Spinner spinner = findViewById(R.id.choose_category);
        spinner.setAdapter(arrayAdapter);

    }

    public void onCreateTaskClicked(View view){

    }

    public void onCalendarClicked(View view){

    }

    public void onDurationClicked(View view){

    }
}