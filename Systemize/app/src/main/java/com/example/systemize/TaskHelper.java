package com.example.systemize;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TaskContract.TaskEntry.TABLE_NAME + "(" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                    TaskContract.TaskEntry.COLUMN_NAME_TASK + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_CATEGORY + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DATE + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_COMPLETED + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tasks.db";

    public TaskHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}