package com.example.systemize;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView nameTitle;
    private TextView name;
    private TextView occupation;
    private TextView bio;
    private ImageView image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpViews(view);
        readDatabase();
        TextView edit = view.findViewById(R.id.edit_profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("First", false);
                Objects.requireNonNull(getActivity()).startActivity(intent);
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                //We start a new activity?
            }
        });
        return view;
    }

    private void setUpViews(View view){
        nameTitle = view.findViewById(R.id.name_title);
        name = view.findViewById(R.id.name);
        occupation = view.findViewById(R.id.occupation);
        bio = view.findViewById(R.id.bio);
        image = view.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }





    private void readDatabase(){
        SettingsHelper helper = new SettingsHelper(Objects.requireNonNull(getActivity()).
                getApplicationContext());

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
            nameTitle.setText(nameString);
            name.setText(nameString);
            occupation.setText(cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_OCCUPATION)));
            bio.setText(cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_BIO)));
            System.out.println(nameString);
            cursor.close();
        }

        cursor = db.rawQuery("select * from imageInfo", null);
        if (cursor.getCount() != 0){
            cursor.moveToLast();
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
            image.setImageBitmap(bitmap);

        }
        else{
            image.setImageResource(R.drawable.user);
        }
        cursor.close();
        helper.close();
    }
}
