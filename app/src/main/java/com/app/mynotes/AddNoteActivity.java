package com.app.mynotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_CODE = 101;
    private static final int RECORD_AUDIO_CODE = 105;

    private Uri cameraPhotoURI = null;

    private Uri audioUri = null;

    private String Location = "";



    private FusedLocationProviderClient fusedLocationClient;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            System.out.println("Location is : "+location.getLatitude());

                            Location = location.getLatitude()+","+location.getLongitude();
                        }
                    }
                });

    }




    public void add_image(View view) {

        Intent photoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        photoIntent.setType("image/*");


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            cameraPhotoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoURI);

        }


        Intent selectOption = Intent.createChooser(photoIntent, "Select Image");
        selectOption.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        startActivityForResult(selectOption, SELECT_IMAGE_CODE);


    }

    public void add_audio(View view)
    {

        Intent pickIntent = new Intent();
        pickIntent.setType("audio/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent recordIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, pickIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Record");
        Intent[] intentarray= {recordIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentarray);
        startActivityForResult(chooser , RECORD_AUDIO_CODE);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK) {

            if (data != null) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {

                    ImageView imageView = findViewById(R.id.selected_image);

                    System.out.println("gallery photo uri : " + selectedImageUri);

                    cameraPhotoURI = selectedImageUri;

                    imageView.setImageURI(selectedImageUri);
                }
            } else {

                ImageView imageView = findViewById(R.id.selected_image);

                System.out.println("camera photo uri : " + cameraPhotoURI);

                imageView.setImageURI(cameraPhotoURI);


            }

        }

        if(requestCode == RECORD_AUDIO_CODE && resultCode == RESULT_OK)
        {
            audioUri = data.getData();

            System.out.println("audio uri : " + audioUri);

            TextView audio_selected = findViewById(R.id.audio_selected_text);

            audio_selected.setVisibility(View.VISIBLE);

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String cameraImageUri = image.getAbsolutePath();
        return image;
    }


    public void add_note(View view) {

        EditText title_et = findViewById(R.id.title_id);

        EditText text_et = findViewById(R.id.text_id);

        String title = title_et.getText().toString().trim();

        String text = text_et.getText().toString().trim();

        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());



        SharedPreferences sharedPreferences = getSharedPreferences("APP_DATA" , MODE_PRIVATE);

        int userId = sharedPreferences.getInt("userId" , 0);

        int subject_id = getIntent().getIntExtra("subject_id" , 0);

        String photoPath = "";

        String audioPath = "";

        if(cameraPhotoURI != null)
        {
            photoPath =  cameraPhotoURI.toString();
        }

        if(audioUri != null)
        {
            audioPath = audioUri.toString();
        }


        if(title.equalsIgnoreCase("") || text.equalsIgnoreCase(""))
        {
            Toast.makeText(AddNoteActivity.this , "Please fill all fields" , Toast.LENGTH_SHORT).show();

        }

        else {




            DatabaseManager db = new DatabaseManager(AddNoteActivity.this);

            db.open();

            db.insertNote(title , date , text , photoPath , audioPath ,Location , subject_id , userId );

            db.close();

            Toast.makeText(AddNoteActivity.this , "Note added successfully" , Toast.LENGTH_SHORT).show();

            finish();

        }

    }

    public void finish(View view) {

        finish();
    }
}
