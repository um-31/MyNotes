package com.app.mynotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mynotes.dataModels.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private RecyclerView note_list;

    private List<Note> note_data ;

    private Adapter adapter ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        note_data = new ArrayList<>();

        note_list = findViewById(R.id.notes_list);

        note_list.setLayoutManager(new LinearLayoutManager(NotesActivity.this , RecyclerView.VERTICAL , false));

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION},
                    202);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant

            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        DatabaseManager db = new DatabaseManager(NotesActivity.this);

        db.open();

        // get user id

        SharedPreferences sharedPreferences = getSharedPreferences("APP_DATA" , MODE_PRIVATE);

        int user_id = sharedPreferences.getInt("userId" , 0);

        int subject_id = getIntent().getIntExtra("subject_id" , 0);

        note_data = db.getNotes(subject_id , user_id);

        db.close();

        adapter = new Adapter();

        note_list.setAdapter(adapter);
    }

    public void add_note(View view) {


        Intent i = new Intent(NotesActivity.this , AddNoteActivity.class);

        i.putExtra("subject_id" , getIntent().getIntExtra("subject_id" , 0));

        startActivity(i);

    }

    public void finish(View view) {

        finish();
    }

    public void search(View view) {

        EditText search_et = findViewById(R.id.search_et);

        String search = search_et.getText().toString().trim();

        if(!search.equalsIgnoreCase(""))
        {
            DatabaseManager db = new DatabaseManager(NotesActivity.this);

            db.open();

            SharedPreferences sharedPreferences = getSharedPreferences("APP_DATA" , MODE_PRIVATE);

            int user_id = sharedPreferences.getInt("userId" , 0);

            int subject_id = getIntent().getIntExtra("subject_id" , 0);

            note_data = db.searchNotes(subject_id , user_id , search);

            db.close();

            adapter = new Adapter();

            note_list.setAdapter(adapter);

        }

        else {


            DatabaseManager db = new DatabaseManager(NotesActivity.this);

            db.open();

            SharedPreferences sharedPreferences = getSharedPreferences("APP_DATA" , MODE_PRIVATE);

            int user_id = sharedPreferences.getInt("userId" , 0);

            int subject_id = getIntent().getIntExtra("subject_id" , 0);

            note_data = db.getNotes(subject_id , user_id );

            db.close();

            adapter = new Adapter();

            note_list.setAdapter(adapter);

        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView title , text , date ;

        LinearLayout note_layout ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);

            text = itemView.findViewById(R.id.text);

            date = itemView.findViewById(R.id.date);

            note_layout = itemView.findViewById(R.id.note_layout);

        }
    }


    private class Adapter extends RecyclerView.Adapter<ViewHolder>
    {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(NotesActivity.this).inflate(R.layout.note_cell , parent , false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Note note = note_data.get(position);

            holder.title.setText(note.TITLE);

            holder.text.setText(note.DESCRIPTION);

            holder.date.setText(note.DATE);

            holder.note_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(NotesActivity.this , ViewNotes.class);

                    i.putExtra("note_id" , note.NOTE_ID);

                    startActivity(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return note_data.size();
        }
    }
}
