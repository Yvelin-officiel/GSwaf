package com.example.gswaf.Database;

public final class DBContract {

    public static class Form {

        public static final String TABLE_USER = "user";
        public static final String COLUMN_USER_ID = "userid";  //Clé primaire
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";

        public static final String SQL_CREATE_TABLE_USER  =
                "CREATE TABLE " + TABLE_USER + " (" +
                        COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_USERNAME + " TEXT ," +
                        COLUMN_PASSWORD + " TEXT )";


        public static final String TABLE_LIKE = "tablelike";
        public static final String COLUMN_LIKE_ID = "likeid";   //Clé primaire
        public static final String COLUMN_LIKE_COCKTAIL_ID = "likecocktailid";
        public static final String COLUMN_LIKE_COCKTAIL_NAME = "likecocktailname";
        public static final String COLUMN_LIKE_COCKTAIL_IMAGE = "likecocktailimage";
        public static final String COLUMN_LIKE_USER_ID = "likeuserid";  //Clé étrangère

        public static final String SQL_CREATE_TABLE_LIKE =
                "CREATE TABLE " + TABLE_LIKE + " (" +
                        COLUMN_LIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_LIKE_COCKTAIL_ID + " INTEGER ," +
                        COLUMN_LIKE_COCKTAIL_NAME + " TEXT ," +
                        COLUMN_LIKE_COCKTAIL_IMAGE + " TEXT ," +
                        COLUMN_LIKE_USER_ID + " INTEGER, " +
                        " FOREIGN KEY ("+COLUMN_LIKE_USER_ID+") REFERENCES "
                        +TABLE_USER+"("+COLUMN_USER_ID+"));";


        public static final String TABLE_LATER = "later";
        public static final String COLUMN_LATER_ID = "laterid"; //Clé primaire
        public static final String COLUMN_LATER_COCKTAIL_ID = "latercocktailid";
        public static final String COLUMN_LATER_USER_ID = "lateruserid";    //Clé étrangère


        public static final String TABLE_LIST = "list";
        public static final String COLUMN_LIST_ID = "listid";   //Clé primaire
        public static final String COLUMN_LIST_COCKTAIL_ID = "listcocktailid";
        public static final String COLUMN_LIST_USER_ID = "listuserid";  //Clé étrangère

    }

}
