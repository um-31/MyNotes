package com.app.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mynotes.dataModels.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {

    private RecyclerView subject_list;

    private List<Subject> subject_data ;

    private Adapter adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        subject_data = new ArrayList<>();

        subject_list = findViewById(R.id.subject_list);

        subject_list.setLayoutManager(new LinearLayoutManager(SubjectsActivity.this , RecyclerView.VERTICAL , false));



    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseManager db = new DatabaseManager(SubjectsActivity.this);

        db.open();

        // get user id

        SharedPreferences sharedPreferences = getSharedPreferences("APP_DATA" , MODE_PRIVATE);

        int user_id = sharedPreferences.getInt("userId" , 0);

        subject_data = db.getSubjects(user_id);

        adapter = new Adapter();

        subject_list.setAdapter(adapter);


    }

    public void add_subject(View view) {

        Intent i = new Intent(SubjectsActivity.this , AddSubject.class);

        startActivity(i);
    }

    public void finish(View view) {

        finish();
    }


    private class ViewHolder extends RecyclerView.ViewHolder{

        TextView subject_name ;

        LinearLayout subject_layout ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject_name = itemView.findViewById(R.id.subject_name);

            subject_layout = itemView.findViewById(R.id.subject_layout);

        }
    }


    private class Adapter extends RecyclerView.Adapter<ViewHolder>
    {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(SubjectsActivity.this).inflate(R.layout.subject_cell , parent , false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Subject subject = subject_data.get(position);

            holder.subject_name.setText(subject.SUBJECT_NAME);

            holder.subject_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(SubjectsActivity.this , NotesActivity.class);

                    i.putExtra("subject_id" , subject.SUBJECT_ID);

                    startActivity(i);

                }
            });

        }

        @Override
        public int getItemCount() {
            return subject_data.size();
        }
    }

}
