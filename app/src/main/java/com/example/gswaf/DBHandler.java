package com.example.gswaf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    //change version when upgraded
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Form.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE " + DBContract.Form.TABLE_LIKE + " (" +
                DBContract.Form.COLUMN_LIKE_COCKTAIL_ID+ " INTEGER ," +
                DBContract.Form.COLUMN_LIKE_USER_ID + " TEXT )";
                //+ " FOREIGN KEY ("+DBContract.Form.LIKE_USER_ID+") REFERENCES "
                //+DBContract.Form.TABLE_USER+"("+DBContract.Form.USER_ID+"));";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + DBContract.Form.TABLE_LIKE;
        db.execSQL(query);
        onCreate(db);
    }

    public void insertCocktail(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // insertion create a row and insert it
        ContentValues row = new ContentValues();

        // Renvoit quand même une erreur quand on insert un cocktail déjà présent dans la DB
        try {
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_ID, id);
            // row.put(DBContract.Form.COLUMN_LIKE_USER_ID, " USER ID DE FOU ");
            db.insert(DBContract.Form.TABLE_LIKE,null,row);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e("ERROR", "Déjà présent dans la DB");
        } catch (Exception e){
            Log.e("ERROR", "Autre erreur");
        }
        db.close();
    }


    public List<Cocktail> selectLike() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Cocktail> responses = new ArrayList<>();

        String[] projection = {
                DBContract.Form.COLUMN_LIKE_COCKTAIL_ID,
                DBContract.Form.COLUMN_LIKE_USER_ID,

        };

        Cursor cursor = db.query(
                DBContract.Form.TABLE_LIKE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_USER_ID));

           Cocktail cocktail = new Cocktail(id);

            responses.add(cocktail);
        }

        cursor.close();
        db.close();

        return responses;
    }


}
