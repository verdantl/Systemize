package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class NewTaskActivity extends AppCompatActivity {

    private TextView date;
    private TextView duration;
    private EditText taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        String[] categories = {"Work", "Personal", "Social", "Finances", "Family", "School"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner,
                new ArrayList<>(Arrays.asList(categories)));
        Spinner spinner = findViewById(R.id.choose_category);
        spinner.setAdapter(arrayAdapter);

        date = findViewById(R.id.date);
        duration = findViewById(R.id.duration);
        taskName = findViewById(R.id.task_name);

    }

    public void onCreateTaskClicked(View view){
    }

    public void onCalendarClicked(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
    }

    public void onDurationClicked(View view){

    }
}