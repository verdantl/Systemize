package com.example.systemize;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeeklyHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + WeeklyContract.WeeklyEntry.TABLE_NAME + "(" +
                    WeeklyContract.WeeklyEntry._ID + " INTEGER PRIMARY KEY, " +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_DAY + " TEXT," +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_WORK + " TEXT," +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_PERSONAL + " TEXT," +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_SOCIAL + " TEXT," +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_FINANCES + " TEXT," +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_FAMILY + " TEXT," +
                    WeeklyContract.WeeklyEntry.COLUMN_NAME_SCHOOL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeeklyContract.WeeklyEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tasks.db";

    public WeeklyHelper(Context context) {
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