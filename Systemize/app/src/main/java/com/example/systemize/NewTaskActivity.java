package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView date;
    private TextView duration;
    private EditText taskName;
    private Spinner spinner;
    private String category;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        String[] categories = {"Work", "Personal", "Social", "Finances", "Family", "School"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner,
                new ArrayList<>(Arrays.asList(categories)));
        spinner = findViewById(R.id.choose_category);
        spinner.setAdapter(arrayAdapter);
        setUp();

    }

    private void setUp(){
        date = findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, LocalDate.now().getYear());
        c.set(Calendar.MONTH, LocalDate.now().getMonthValue());
        c.set(Calendar.DAY_OF_MONTH, LocalDate.now().getDayOfMonth());
        date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime()));
        duration = findViewById(R.id.duration);
        duration.setText("All Day");
        taskName = findViewById(R.id.task_name);
//        setUpDatabase();

    }

    private void setUpDatabase(){
        TaskHelper taskHelper = new TaskHelper(getApplicationContext());
        db = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_TASK, String.valueOf(taskName.getText()));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_CATEGORY, String.valueOf(category));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_DATE, String.valueOf(date.getText()));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, false);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null,
                contentValues);
    }

    public void onCreateTaskClicked(View view){
        taskName.getText();
    }

    public void onCalendarClicked(View view){
        DialogFragment datePickerDialog = new DatePickerFragment();

        datePickerDialog.show(getSupportFragmentManager(), "Date Picker");
    }

    public void onDurationClicked(View view){

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date.setText(currentDateString);
        System.out.println(date.getText());
    }
}