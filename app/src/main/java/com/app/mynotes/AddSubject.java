package com.app.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubject extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
    }

    public void add_subject(View view) {


        EditText subject_et = findViewById(R.id.subject_name);

        String subject_name = subject_et.getText().toString().trim();

        if( subject_name.equalsIgnoreCase(""))
        {

            Toast.makeText(AddSubject.this , "Please enter subject name" , Toast.LENGTH_SHORT).show();


        }

        else {

            // get user ID from shared preferences

            SharedPreferences sharedPreferences = getSharedPreferences("APP_DATA" , MODE_PRIVATE);

            int userId = sharedPreferences.getInt("userId" , 0);

            // add subject data into database

            DatabaseManager db = new DatabaseManager(AddSubject.this);

            db.open();

            db.insertSubject(subject_name , userId);

            db.close();

            Toast.makeText(AddSubject.this , "Subject added successfully" , Toast.LENGTH_SHORT).show();

            finish();


        }


    }

    public void finish(View view) {

        finish();
    }
}
