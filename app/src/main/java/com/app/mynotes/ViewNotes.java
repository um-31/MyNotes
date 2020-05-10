package com.app.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mynotes.dataModels.Note;

public class ViewNotes extends AppCompatActivity {

    private Boolean audioPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);


        getNote();

    }

    public void getNote()
    {

        int note_id = getIntent().getIntExtra("note_id" , 0);

        DatabaseManager db = new DatabaseManager(ViewNotes.this);

        db.open();

        final Note note = db.getSingleNote(note_id);

        EditText title_et = findViewById(R.id.title_id);

        EditText text_et = findViewById(R.id.text_id);

        TextView date = findViewById(R.id.date);

        TextView location = findViewById(R.id.location);

        date.setText("Created on : "+note.DATE);

        if(!note.LOCATION.trim().equalsIgnoreCase(""))
        {
            location.setText("Location on map");
            location.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = "http://maps.google.com/maps?daddr=" + note.LOCATION;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            });
        }

        else {

            location.setText("Location not found");
        }

        ImageView photo = findViewById(R.id.selected_image);

        title_et.setText(note.TITLE);

        text_et.setText(note.DESCRIPTION);

        if(!note.PHOTO.equalsIgnoreCase("")) {
            photo.setImageURI(Uri.parse(note.PHOTO));
        }

        if(!note.AUDIO.equalsIgnoreCase(""))
        {
            ImageView playImage = findViewById(R.id.play_audio);

            playImage.setVisibility(View.VISIBLE);

            final MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(note.AUDIO));

            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(!audioPlaying)
                    {

                        mediaPlayer.start();

                        audioPlaying = true;

                    }

                    else {

                        audioPlaying = false;

                        mediaPlayer.stop();
                    }



                }
            });
        }


    }

    public void finish(View view) {

        finish();
    }
}
