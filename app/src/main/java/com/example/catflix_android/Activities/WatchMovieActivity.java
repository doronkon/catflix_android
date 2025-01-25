package com.example.catflix_android.Activities;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.R;

public class WatchMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Set the content view to the layout
        setContentView(R.layout.activity_watch_movie);

        // Edge to edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the movie data passed from the previous activity
        Movie currMovie = (Movie) getIntent().getSerializableExtra("movie");

        // Get the VideoView from the layout
        VideoView videoView = findViewById(R.id.videoView);

        // Construct the video URL (replace with your dynamic URL if needed)
        String baseUrl = "http://10.0.2.2:8080/media/actualMovies/";
        String videoUrl = baseUrl + currMovie.getPathToMovie();

        // Set up the VideoView
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Add MediaController for playback controls (optional)
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Start the video playback once the video is prepared
        videoView.setOnPreparedListener(mp -> videoView.start());

        // Handle errors (optional)
        videoView.setOnErrorListener((mp, what, extra) -> {
            // Log or show error message
            return true;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Restore portrait orientation if needed
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
