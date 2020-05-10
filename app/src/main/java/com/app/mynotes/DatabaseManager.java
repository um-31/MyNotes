package com.app.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.mynotes.dataModels.Note;
import com.app.mynotes.dataModels.Subject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private SQLiteDatabaseHelper sqlHelper ;

    private Context context;

    private SQLiteDatabase database ;

    public DatabaseManager(Context context)
    {
        this.context = context;
    }

    public DatabaseManager open() {

        sqlHelper = new SQLiteDatabaseHelper(context);

        database = sqlHelper.getWritableDatabase();

        return this;

    }

    public void close()
    {

        sqlHelper.close();
    }

    public void insertUser(String name , String email , String password)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(sqlHelper.NAME , name);
        contentValues.put(sqlHelper.EMAIl , email);
        contentValues.put(sqlHelper.PASSWORD , password);

        database.insert(sqlHelper.USER_TABLE_NAME , null , contentValues);
    }

    public int userLogin(String email , String password)
    {
        Cursor c = database.rawQuery("SELECT * FROM "+sqlHelper.USER_TABLE_NAME+" WHERE "+sqlHelper.EMAIl+" = '"+ email.trim()+"' and "+sqlHelper.PASSWORD+" = '"+password.trim()+"'" , null);

        if(c.moveToFirst()) {

            return c.getInt(0);
        }

        else {

            return -1 ;
        }

    }

    public void insertSubject(String name , int userId)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(sqlHelper.SUBJECT_NAME , name);
        contentValues.put(sqlHelper.SUBJECT_USER_ID , userId);

        database.insert(sqlHelper.SUBJECT_TABLE_NAME , null , contentValues);

    }

    public List<Subject> getSubjects(int userId)
    {

        List<Subject> subjects = new ArrayList<>();

        Cursor c = database.rawQuery("SELECT * FROM "+sqlHelper.SUBJECT_TABLE_NAME+" WHERE "+sqlHelper.SUBJECT_USER_ID+" = '"+ userId +"'" , null);

        if(c.moveToFirst()) {

            do{

                Subject subject = new Subject();

                subject.SUBJECT_ID = c.getInt(0);
                subject.SUBJECT_NAME = c.getString(1);
                subject.USER_ID = c.getInt(2);

                subjects.add(subject);

            } while (c.moveToNext());
        }


        return subjects;

    }

    public void insertNote( String title , String date , String  text , String photo , String audio , String location , int subject_id , int user_id)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(sqlHelper.TITLE , title);
        contentValues.put(sqlHelper.DATE , date);
        contentValues.put(sqlHelper.DESCRIPTION , text);
        contentValues.put(sqlHelper.PHOTO , photo);
        contentValues.put(sqlHelper.AUDIO , audio);
        contentValues.put(sqlHelper.LOCATION , location);
        contentValues.put(sqlHelper.NOTES_SUBJECT_ID , subject_id);
        contentValues.put(sqlHelper.NOTES_USER_ID , user_id);

        database.insert(sqlHelper.NOTES_TABLE_NAME , null , contentValues);

    }

    public List<Note> getNotes ( int subject_id , int user_id)
    {
        List<Note> notes = new ArrayList<>();

        Cursor c = database.rawQuery("SELECT * FROM "+sqlHelper.NOTES_TABLE_NAME+" WHERE "+sqlHelper.NOTES_SUBJECT_ID+" = '"+ subject_id+"' and "+sqlHelper.NOTES_USER_ID+" = '"+user_id+"'" , null);

        if(c.moveToFirst()) {

            do {

                Note note = new Note();

                note.NOTE_ID = c.getInt(0);
                note.TITLE = c.getString(1);
                note.DATE = c.getString(2);
                note.DESCRIPTION = c.getString(3);
                note.PHOTO = c.getString(4);
                note.AUDIO = c.getString(5);
                note.LOCATION = c.getString(6);
                note.SUBJECT_ID = c.getInt(7);
                note.USER_ID = c.getInt(8);

                notes.add(note);

            } while (c.moveToNext());

        }

        return notes;

    }


    public List<Note> searchNotes ( int subject_id , int user_id , String search)
    {
        List<Note> notes = new ArrayList<>();

        Cursor c = database.rawQuery("SELECT * FROM "+sqlHelper.NOTES_TABLE_NAME+" WHERE "+sqlHelper.NOTES_SUBJECT_ID+" = '"+ subject_id+"' and "+sqlHelper.NOTES_USER_ID+" = '"+user_id+"' and ( "+sqlHelper.TITLE+" LIKE '%"+search+"%' or "+sqlHelper.DESCRIPTION+" LIKE '%"+search+"%' )" , null);

        if(c.moveToFirst()) {

            do {

                Note note = new Note();

                note.NOTE_ID = c.getInt(0);
                note.TITLE = c.getString(1);
                note.DATE = c.getString(2);
                note.DESCRIPTION = c.getString(3);
                note.PHOTO = c.getString(4);
                note.AUDIO = c.getString(5);
                note.LOCATION = c.getString(6);
                note.SUBJECT_ID = c.getInt(7);
                note.USER_ID = c.getInt(8);

                notes.add(note);

            } while (c.moveToNext());

        }

        return notes;

    }

    public Note getSingleNote ( int note_id)
    {


        Cursor c = database.rawQuery("SELECT * FROM "+sqlHelper.NOTES_TABLE_NAME+" WHERE "+sqlHelper.NOTES_ID+" = '"+ note_id+"'" , null);

        if(c.moveToFirst()) {



                Note note = new Note();

                note.NOTE_ID = c.getInt(0);
                note.TITLE = c.getString(1);
                note.DATE = c.getString(2);
                note.DESCRIPTION = c.getString(3);
                note.PHOTO = c.getString(4);
                note.AUDIO = c.getString(5);
                note.LOCATION = c.getString(6);
                note.SUBJECT_ID = c.getInt(7);
                note.USER_ID = c.getInt(8);

               return note;



        }

        return null;

    }

}
