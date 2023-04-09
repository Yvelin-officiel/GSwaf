package com.example.gswaf.Database;

import static com.example.gswaf.Database.DBContract.Form.COLUMN_LIKE_COCKTAIL_ID;
import static com.example.gswaf.Database.DBContract.Form.COLUMN_LIKE_USER_ID;
import static com.example.gswaf.Database.DBContract.Form.TABLE_LIKE;

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

    /**
     * Execute les requêtes de créations des tables
     * @param db La base dans laquelle ajouter
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABLE USER
        db.execSQL(DBContract.Form.SQL_CREATE_TABLE_USER);

        // TABLE LIKE
        db.execSQL(DBContract.Form.SQL_CREATE_TABLE_LIKE);

    }

    /**
     * Supprime les tables dans la db si elles existent déjà, puis les recréer
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query1 = "DROP TABLE IF EXISTS " + DBContract.Form.TABLE_USER;
        db.execSQL(query1);

        String query2 = "DROP TABLE IF EXISTS " + TABLE_LIKE;
        db.execSQL(query2);

        onCreate(db);
    }

    /**
     * Créer un compte utilisateur dans la DB
     * @param username  Le nom du compte
     * @param password  Le mot de passe associé
     * @return  True si l'ajout s'est bien déroulé, false sinon
     */
    public boolean insertUser(String username, String password) {
        long result = -1;
        try{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(DBContract.Form.COLUMN_USERNAME, username);
        row.put(DBContract.Form.COLUMN_PASSWORD, password);
        result = db.insert(DBContract.Form.TABLE_USER, null, row);

        }catch (Exception e) {
            Log.e("ERROR", "Erreur d'insertion");
        }

        if (result == -1) return false;
        else return true;

    }

    /**
     * Ajoute les infos du cocktail dans la colonne qui correspond
     * @param id    L'id du cocktail
     * @param name  Le nom du cocktail
     * @param imageURL  L'url de l'image
     * @param userId    L'id de l'utilisateur connecté
     */
    public void insertLike(int id, String name, String imageURL, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // insertion create a row and insert it
        ContentValues row = new ContentValues();

        try {
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_ID, id);
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_NAME, name);
            row.put(DBContract.Form.COLUMN_LIKE_COCKTAIL_IMAGE, imageURL);
            row.put(DBContract.Form.COLUMN_LIKE_USER_ID, userId);
            db.insert(TABLE_LIKE, null, row);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Log.e("ERROR", "Déjà présent dans la DB");
        } catch (Exception e) {
            Log.e("ERROR", "Autre erreur");
        }
    }

    /**
     * Supprime la ligne d'un cocktail
     * @param cocktailId    L'id du cocktail à supprimer
     * @param userId    L'id de l'utilisateur qui souhaite supprimer
     * @return  True si aucun problème, False sinon
     */
    public boolean deleteLike(int cocktailId, int userId) {
        boolean success = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("DELETE FROM "+ TABLE_LIKE + "" +
                    " WHERE " + COLUMN_LIKE_COCKTAIL_ID + " = "+cocktailId+"" +
                    " AND " + COLUMN_LIKE_USER_ID + " = " + userId + "");
            db.close();
            success = true;
        } catch (Exception e) {
            Log.e("ERROR", "Erreur Delete in Database");
        }
        return success;
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
                TABLE_LIKE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Verifie chaque cocktail de la DB
        while (cursor.moveToNext()) {

            int idUser = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_USER_ID));

            if (idUser == userID){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_NAME));
                String imageURL = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.Form.COLUMN_LIKE_COCKTAIL_IMAGE));
                Cocktail cocktail = new Cocktail(id);
                cocktail.setName(name);
                cocktail.setImageURL(imageURL);
                responses.add(cocktail);
            }
        }
        cursor.close();
        return responses;
    }

    /**
     * Cherche l'id de l'utilisateur rentré en paramètre
     * @param username
     * @return l'id
     */
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

    /**
     * Vérifie si le nom d'utilisateur est déjà présent dans la DB
     * @param username Le nom à chercher
     * @return true si il existe, false sinon
     */
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

    /**
     * Vérifie si le nom de compte et le mot de passe correspondent dans la DB
     * @param username Le nom de compte
     * @param password Le mot de passe associer
     * @return True si c'est bon, false sinon
     */
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
