package com.example.systemize;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        readDatabase(view);
        TextView edit = view.findViewById(R.id.edit_profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We start a new activity?
            }
        });
        return view;
    }

    private void readDatabase(View view){
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
        cursor.moveToFirst();

        TextView nameTitle = view.findViewById(R.id.name_title);
        TextView name = view.findViewById(R.id.name);
        TextView occupation = view.findViewById(R.id.occupation);
        TextView bio = view.findViewById(R.id.bio);
        String nameString = cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_NAME));

        nameTitle.setText(nameString);
        name.setText(nameString);
        occupation.setText(cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_OCCUPATION)));
        bio.setText(cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_BIO)));
    }
}
