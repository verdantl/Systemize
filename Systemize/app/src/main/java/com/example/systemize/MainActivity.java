package com.example.systemize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private boolean first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

    }
    private void readDatabase(){
        SettingsHelper helper = new SettingsHelper(getApplicationContext());

        SQLiteDatabase db = helper.getReadableDatabase();
        String sortOrder =
                BaseColumns._ID + " DESC";

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query(
                SettingsContract.SettingsEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        boolean value = cursor.moveToFirst();
        if (value){
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
        }
        else{
            if (!first) {
                setContentView(R.layout.activity_task_selection);
                first = true;
            }
            else{
                Intent intent = new Intent(this, OnboardingActivity1.class);
                startActivity(intent);
            }

        }

        cursor.close();
        helper.close();
    }

    public void onStartClicked(View view){
        readDatabase();
    }

}