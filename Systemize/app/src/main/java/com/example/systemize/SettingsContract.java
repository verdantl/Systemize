package com.example.systemize;

import android.provider.BaseColumns;

public final class SettingsContract {
    private SettingsContract(){

    }

    public static class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Settings";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_PRODUCTIVITY = "Productivity";
        public static final String COLUMN_NAME_IMAGE = "Image";
        public static final String COLUMN_NAME_OCCUPATION = "Occupation";
        public static final String COLUMN_NAME_BIO = "Bio";
    }
}