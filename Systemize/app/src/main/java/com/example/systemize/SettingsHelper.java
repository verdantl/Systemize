package com.example.systemize;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME + "(" +
                    SettingsContract.SettingsEntry._ID + " INTEGER PRIMARY KEY, " +
                    SettingsContract.SettingsEntry.COLUMN_NAME_NAME + " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_PRODUCTIVITY + " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_IMAGE + " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_OCCUPATION + " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_BIO+ " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_PRODUCTIVITY + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Tasks.db";

    public SettingsHelper(Context context) {
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