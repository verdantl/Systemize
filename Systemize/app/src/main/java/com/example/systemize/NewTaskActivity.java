package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView date;
    private TextView duration;
    private EditText taskName;
    private Spinner spinner;
    private String category;
    private LocalDate saveDate;

    private SQLiteDatabase db;
    private boolean canSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        saveDate = LocalDate.now();
        final String[] categories = {"Work", "Personal                  ",
                "Social", "Finances", "Family", "School", "Other"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner,
                new ArrayList<>(Arrays.asList(categories)));
        spinner = findViewById(R.id.spinner_category);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categories[i];
                canSave = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                canSave = false;
            }
        });
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
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        duration.setText(dateFormat.format(new Date()));
        taskName = findViewById(R.id.task_name);

    }


    private void writeToDatabase(){
        TaskHelper taskHelper = new TaskHelper(getApplicationContext());
        db = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_TASK, String.valueOf(taskName.getText()));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_CATEGORY, String.valueOf(category));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_DATE, saveDate.toString());
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_DURATION, duration.getText().toString());
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_COMPLETED, false);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null,
                contentValues);
        taskHelper.close();
    }

    public void onCreateTaskClicked(View view){
        if (canSave){
            writeToDatabase();
            finish();
        }
        else{
            Toast.makeText(this, "You must pick a category.", Toast.LENGTH_LONG).show();
        }
    }

    public void onCalendarClicked(View view){
        DialogFragment datePickerDialog = new DatePickerFragment();

        datePickerDialog.show(getSupportFragmentManager(), "Date Picker");
    }

    public void onDurationClicked(View view){
        DialogFragment timePickerDialog  = new TimePickerFragment();
        timePickerDialog.show(getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        saveDate = LocalDate.parse(format.format(c.getTime()));
        date.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
        String temp = LocalTime.MIN.plus(Duration.ofMinutes(hours * 60 + minutes)).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date time = sdf.parse(temp);
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
            assert time != null;
            duration.setText(sdf2.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}