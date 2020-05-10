package com.app.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void create_account(View view) {

        EditText name_et = findViewById(R.id.name_et);
        EditText email_et = findViewById(R.id.email_et);
        EditText password_et = findViewById(R.id.password_et);

        String name = name_et.getText().toString().trim();
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();


        if(name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
        {
            Toast.makeText(SignupActivity.this , "Please enter all fields" , Toast.LENGTH_SHORT).show();

        }

        else {

          DatabaseManager db =  new DatabaseManager(SignupActivity.this);

          db.open();

          db.insertUser(name , email , password);

          db.close();

            Toast.makeText(SignupActivity.this , "Account created successfully , please login" , Toast.LENGTH_SHORT).show();


            finish();
        }



    }
}
