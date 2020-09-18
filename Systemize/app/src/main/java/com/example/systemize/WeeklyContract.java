package com.example.systemize;

import android.provider.BaseColumns;

public final class WeeklyContract {
    private WeeklyContract(){

    }

    public static class WeeklyEntry implements BaseColumns {
        public static final String TABLE_NAME = "Weekly";
        public static final String COLUMN_NAME_DAY = "Day";
        public static final String COLUMN_NAME_WORK = "Work";
        public static final String COLUMN_NAME_PERSONAL = "Personal";
        public static final String COLUMN_NAME_SOCIAL = "Social";
        public static final String COLUMN_NAME_FINANCES = "Finances";
        public static final String COLUMN_NAME_FAMILY = "Family";
        public static final String COLUMN_NAME_SCHOOL = "School";
    }
}
