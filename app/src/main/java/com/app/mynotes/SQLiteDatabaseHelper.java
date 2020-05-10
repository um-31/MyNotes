package com.app.mynotes;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "MY_NOTES.DB";

    static final int DB_VERSION = 1;

    //user table name

    public static final String USER_TABLE_NAME = "USERS";

    // user table columns

    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String EMAIl = "email";
    public static final String PASSWORD = "password";

    // subject table name

    public static final String SUBJECT_TABLE_NAME = "SUBJECTS";

    // subject table columns

    public static final String SUBJECT_ID = "subject_id";
    public static final String SUBJECT_NAME = "subject_name";
    public static final String SUBJECT_USER_ID = "user_id";

    //notes table name

    public static final String NOTES_TABLE_NAME = "NOTES";

    // notes table columns

    public static final String NOTES_ID = "notes_id";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String PHOTO = "photo";
    public static final String AUDIO = "audio";
    public static final String LOCATION = "location";
    public static final String NOTES_SUBJECT_ID = "subject_id";
    public static final String NOTES_USER_ID = "user_id";

    // Create user table query

    private static final String CREATE_USER_TABLE = "create table " +
            USER_TABLE_NAME +"("+
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NAME + " TEXT NOT NULL, "+
            EMAIl + " TEXT NOT NULL, "+
            PASSWORD + " TEXT NOT NULL );";

    // create subject table query

    private static final String CREATE_SUBJECT_TABLE = "create table "+
            SUBJECT_TABLE_NAME +"("+
            SUBJECT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            SUBJECT_NAME +" TEXT NOT NULL, "+
            SUBJECT_USER_ID +" INTEGER );";

    //create notes table query

    private static final String CREATE_NOTES_TABLE = "create table "+
            NOTES_TABLE_NAME +" ("+
            NOTES_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TITLE +" TEXT NOT NULL, "+
            DATE +" TEXT NOT NULL, "+
            DESCRIPTION +" TEXT, "+
            PHOTO +" TEXT, "+
            AUDIO +" TEXT, "+
            LOCATION +" TEXT, "+
            NOTES_SUBJECT_ID +" INTEGER NOT NULL, "+
            NOTES_USER_ID + " INTEGER NOT NULL );";


    public SQLiteDatabaseHelper( Context context ) {

        super(context, DATABASE_NAME , null , DB_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_SUBJECT_TABLE);
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+SUBJECT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NOTES_TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
