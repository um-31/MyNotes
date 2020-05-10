package com.app.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {

        EditText email_et = findViewById(R.id.email_et);
        EditText password_et = findViewById(R.id.password_et);


        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        if(email.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
        {

            Toast.makeText(LoginActivity.this , "Please enter all fields" , Toast.LENGTH_SHORT).show();

        }
        else {

            DatabaseManager db = new DatabaseManager(LoginActivity.this);

            db.open();

            int user_id = db.userLogin(email , password);

            if(user_id > -1)
            {

                // save the user ID in shared preference

                SharedPreferences.Editor shared_pref = getSharedPreferences("APP_DATA" , MODE_PRIVATE).edit();

                shared_pref.putInt("userId" , user_id);

                shared_pref.commit();

                Toast.makeText(LoginActivity.this , "Logged in successfully" , Toast.LENGTH_SHORT).show();

                Intent i = new Intent(LoginActivity.this , SubjectsActivity.class);

                startActivity(i);

                finishAffinity();

            }

            else {

                Toast.makeText(LoginActivity.this , "Invalid login try again" , Toast.LENGTH_SHORT).show();


            }

        }

    }
}
