package com.example.kr2rpois;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {

    private final MainActivity mainActivity;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactDb23";
    public static final String TABLE_CONTACTS = "contacts23";

    public static final String KEY_ID = "_id";
    public static final String KEY_HEADING = "head";
    public static final String KEY_TEXT = "text";
    public static final String KEY_TIME_MINS = "time_mins";
    public static final String KEY_TIME_OURS = "time_ours";

    public DBHelper(MainActivity mainActivity, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID + " integer primary key,"
                + KEY_HEADING + " text," + KEY_TEXT + " text," + KEY_TIME_OURS + " integer," + KEY_TIME_MINS + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
}
