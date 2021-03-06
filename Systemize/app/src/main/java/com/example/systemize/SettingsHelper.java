package com.example.systemize;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class SettingsHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME + "(" +
                    SettingsContract.SettingsEntry._ID + " INTEGER PRIMARY KEY, " +
                    SettingsContract.SettingsEntry.COLUMN_NAME_NAME + " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_OCCUPATION + " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_BIO+ " TEXT," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_PRODUCTIVITY + " TEXT)";

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Settings.db";

    private static String createTableQuery = "create table if not exists imageInfo (image BLOB)";

    private Context context;

    public SettingsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(createTableQuery);
    }

    public void storeImage(ModelClass objectModelClass){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = objectModelClass.getImage();
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
            imageInBytes = objectByteArrayOutputStream.toByteArray();
            if (imageInBytes.length > 2000000) {
                Toast.makeText(context, "This image is too large.", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("image", imageInBytes);
                long value = db.insert("imageInfo", null, contentValues);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}