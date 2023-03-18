package com.example.gswaf;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    //change version when upgraded
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Form.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DBContract.Form.TABLE_NAME + " (" +
                DBContract.Form._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.Form.COLUMN_LIKES + " TEXT," +
                DBContract.Form.COLUMN_LATER + " TEXT," +
                DBContract.Form.COLUMN_LIST + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + DBContract.Form.TABLE_NAME;
        onCreate(db);
    }

    public void insertForm (String likes, String later, String list ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(DBContract.Form.COLUMN_LIKES,likes);
        row.put(DBContract.Form.COLUMN_LATER,later);
        row.put(DBContract.Form.COLUMN_LIST,list);
// return row primary key
        long newRowId = db.insert(DBContract.Form.TABLE_NAME,null,row);
    }


}
