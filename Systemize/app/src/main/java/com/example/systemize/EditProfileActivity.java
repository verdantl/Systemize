package com.example.systemize;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class EditProfileActivity extends AppCompatActivity {
    private final int REQUEST_CODE_GALLERY = 999;
    private final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private ImageView image;
    private EditText name;
    private EditText occupation;
    private EditText bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpViews();
    }

    private void setUpViews(){
        image = findViewById(R.id.profile_pic);
        findViewById(R.id.change_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        name = findViewById(R.id.name);
        occupation = findViewById(R.id.occupation);
        bio = findViewById(R.id.bio);
        readDatabase();
        Button button = findViewById(R.id.finish_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeImage(view);
                storeInfo();
                finish();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void chooseImage(){
        try{
            Intent intent = new Intent();
            intent.setType("image/*");

            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void storeInfo(){
        SettingsHelper helper = new SettingsHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SettingsContract.SettingsEntry.COLUMN_NAME_NAME, name.getText().toString());
        contentValues.put(SettingsContract.SettingsEntry.COLUMN_NAME_OCCUPATION, occupation.getText().toString());
        contentValues.put(SettingsContract.SettingsEntry.COLUMN_NAME_BIO, bio.getText().toString());
        db.insert(SettingsContract.SettingsEntry.TABLE_NAME, null,
                contentValues);
        helper.close();
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
        if (cursor.moveToFirst()) {


            String nameString = cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_NAME));

            name.setHint(nameString);
            occupation.setHint(cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_OCCUPATION)));
            bio.setHint(cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_BIO)));
            cursor.close();
            helper.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                image.setImageBitmap(imageToStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void storeImage(View view){
        if (image.getDrawable()!=null) {
            SettingsHelper settingsHelper = new SettingsHelper(getApplicationContext());
            settingsHelper.storeImage(new ModelClass("Image", imageToStore));
        }
    }
}