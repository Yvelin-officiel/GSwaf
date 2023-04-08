package com.example.gswaf.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gswaf.JavaClass.Cocktail;
import com.example.gswaf.JavaClass.User;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    //change version when upgraded
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Form.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABLE USER
        db.execSQL(DBContract.Form.SQL_CREATE_TABLE_USER);

        // TABLE LIKE
        db.execSQL(DBContract.Form.SQL_CREATE_TABLE_LIKE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query1 = "DROP TABLE IF EXISTS " + DBContract.Form.TABLE_USER;
        db.execSQL(query1);

        String query2 = "DROP TABLE IF EXISTS " + DBContract.Form.TABLE_LIKE;
        db.execSQL(query2);

        onCreate(db);
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(DBContract.Form.COLUMN_USERNAME, username);
        row.put(DBContract.Form.COLUMN_PASSWORD, password);

        long result = db.insert(DBContract.Form.TABLE_USER, null, row);

        if (result == 1) return false;
        else return true;

    }

    public void insertLike(int id, String name, String imageURL, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // insertion create a row and insert it
        ContentValues row = new ContentValues();

        try {
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_ID, id);
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_NAME, name);
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_IMAGE, imageURL);
            row.put(DBContract.Form.COLUMN_LIKE_USER_ID, userId);
            db.insert(DBContract.Form.TABLE_LIKE, null, row);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e("ERROR", "Déjà présent dans la DB");
        } catch (Exception e) {
            Log.e("ERROR", "Autre erreur");
        }
    }

    /**
     * @return La liste des cocktails aimés par l'utilisateur connecté
     */
    public List<Cocktail> selectLike(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Cocktail> responses = new ArrayList<>();

        String[] projection = {
                DBContract.Form.COLUMN_LIKE_ID,
                DBContract.Form.COLUMN_LIKE_COCKTAIL_ID,
                DBContract.Form.COLUMN_LIKE_COCKTAIL_NAME,
                DBContract.Form.COLUMN_LIKE_COCKTAIL_IMAGE,
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

        System.out.println("USER CONNECTER : "+ userID);

        // Verifie chaque cocktail de la DB
        while (cursor.moveToNext()) {

            int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_USER_ID));

            if (idUser == userID){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_NAME));
                String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_IMAGE));
                Cocktail cocktail = new Cocktail(id);
                cocktail.setName(name);
                responses.add(cocktail);
            }
        }
        cursor.close();
        return responses;
    }


    public int selectUserIdByUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        String[] projection = {
                DBContract.Form.COLUMN_USER_ID,
                DBContract.Form.COLUMN_USERNAME,
                DBContract.Form.COLUMN_PASSWORD,

        };

        Cursor cursor = db.query(
                DBContract.Form.TABLE_USER,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        String uname;
        while (cursor.moveToNext()) {
            uname = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_USERNAME));
            if (username.equals(uname)){
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_USER_ID));
            }
        }
        return userId;
    }

    public boolean alreadyExist(String username) {
        boolean exist = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                DBContract.Form.COLUMN_USERNAME
        };
        Cursor cursor = db.query(
                DBContract.Form.TABLE_USER,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        String uname;
        while (cursor.moveToNext()) {
            uname = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_USERNAME));
            if (username.equals(uname)){
                exist = true;
            }
        }
        cursor.close();
        return exist;
    }

    public boolean checkUsernamePaswword(String username, String password) {
        boolean exist = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                DBContract.Form.COLUMN_USERNAME,
                DBContract.Form.COLUMN_PASSWORD
        };
        Cursor cursor = db.query(
                DBContract.Form.TABLE_USER,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        String uname;
        String pass;
        while (cursor.moveToNext()) {
            uname = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_USERNAME));
            pass = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_PASSWORD));
            if (username.equals(uname) & password.equals(pass)) {
                exist = true;
            }
        }
        cursor.close();
        return exist;
    }




    /**
     * @return La liste complète des utilisateurs
     */
    public List<User> selectUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> responses = new ArrayList<>();

        String[] projection = {
                DBContract.Form.COLUMN_USER_ID,
                DBContract.Form.COLUMN_USERNAME,
                DBContract.Form.COLUMN_PASSWORD,
        };

        Cursor cursor = db.query(
                DBContract.Form.TABLE_USER,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_PASSWORD));

            User user = new User(username, password);
            responses.add(user);
        }
        cursor.close();
        return responses;
    }
}
