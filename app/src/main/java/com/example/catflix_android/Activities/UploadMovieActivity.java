package com.example.catflix_android.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;
import com.example.catflix_android.Utils;
import com.example.catflix_android.ViewModels.MovieViewModel;
import com.example.catflix_android.ViewModels.UserViewModel;

public class UploadMovieActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    private Uri selectedVideoUri;

    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String> videoPickerLauncher;
    private MovieViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_movie_activty);

        final EditText nameEditText = findViewById(R.id.editTextName);
        final EditText directorEditText = findViewById(R.id.editTextDirector);
        final EditText actorsEditText = findViewById(R.id.editTextActors);
        final EditText descriptionEditText = findViewById(R.id.editTextDescription);
        final EditText minimalAgeEditText = findViewById(R.id.editTextMinimalAge);
        final RadioGroup catflixOriginalGroup = findViewById(R.id.radioGroupCatflixOriginal);
        final Button uploadMovieBtn = findViewById(R.id.buttonUploadMovie);

        model = new MovieViewModel(this, this);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Toast.makeText(this, "Image selected: " + uri, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        videoPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedVideoUri = uri;
                Toast.makeText(this, "Video selected: " + uri, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.buttonUploadThumbnail).setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        findViewById(R.id.buttonUploadVideo).setOnClickListener(v -> videoPickerLauncher.launch("video/*"));

        uploadMovieBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String director = directorEditText.getText().toString().trim();
            String actors = actorsEditText.getText().toString().trim();
            String desc = descriptionEditText.getText().toString().trim();
            String minimal = minimalAgeEditText.getText().toString().trim();
            boolean isCatflixOriginal = catflixOriginalGroup.getCheckedRadioButtonId() == R.id.radioTrue;

            if (selectedImageUri == null || selectedVideoUri == null) {
                Toast.makeText(this, "Please upload both a thumbnail and a video", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || director.isEmpty() || actors.isEmpty() || desc.isEmpty() || minimal.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String image = Utils.imageUriToBase64(this, selectedImageUri);
            String video = Utils.videoUriToBase64(this, selectedVideoUri);
            //hard coded category switch to dropDown list
            Movie movie = new Movie(null, video, minimal, isCatflixOriginal, desc, image, null, actors, director, "67966d1e802d173052a8deb2", null, name);

            model.uploadMovie(movie);
        });

        model.getUploadedMovie().observe(this, movie -> {
            if (movie != null) {
                Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Upload failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
