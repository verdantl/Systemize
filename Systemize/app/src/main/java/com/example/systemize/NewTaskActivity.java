package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private LocalDate saveDate;

    private SQLiteDatabase db;
    private boolean canSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        saveDate = LocalDate.now();
        final String[] categories = {"Work", "Personal", "Social", "Finances", "Family", "School", "Other"};
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
        duration.setText("All Day");
        taskName = findViewById(R.id.task_name);
//        setUpDatabase();

    }

    private void writeToDatabase(){
        TaskHelper taskHelper = new TaskHelper(getApplicationContext());
        db = taskHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_TASK, String.valueOf(taskName.getText()));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_CATEGORY, String.valueOf(category));
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME_DATE, saveDate.toString());
        System.out.println(saveDate);
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
        //writeToDatabase();
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        saveDate = LocalDate.parse(format.format(c.getTime()));
        date.setText(currentDateString);
    }

}