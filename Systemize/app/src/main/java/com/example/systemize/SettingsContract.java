package com.example.systemize;

import android.provider.BaseColumns;

public final class SettingsContract {
    private SettingsContract(){

    }

    public static class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Settings";
        public static final String COLUMN_NAME_COLORS = "Colors";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_CATEGORIES = "Categories";
        public static final String COLUMN_NAME_PRODUCTIVITY = "Productivity";
    }
}