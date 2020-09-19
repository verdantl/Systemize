package com.example.systemize;

import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract(){

    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tasks";
        public static final String COLUMN_NAME_TASK = "Task";
        public static final String COLUMN_NAME_CATEGORY = "Category";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_COMPLETED = "Completed";
        public static final String COLUMN_NAME_DURATION = "Duration";
    }
}
