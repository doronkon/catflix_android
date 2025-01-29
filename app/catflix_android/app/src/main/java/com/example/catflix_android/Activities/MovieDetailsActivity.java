package com.example.catflix_android.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CurrentMovieViewModel;
import com.example.catflix_android.ViewModels.CurrentUserViewModel;

public class MovieDetailsActivity extends AppCompatActivity {
    private CurrentMovieViewModel currentMovieViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //here changes
        currentMovieViewModel = new CurrentMovieViewModel(this, this);


        String movieId = getIntent().getStringExtra("movie_id");
        currentMovieViewModel.getCurrentMovie().observe(this, movie->{
            //init fields of movie
            TextView currentID = findViewById(R.id.currentID);
            currentID.setText(movie.get_id());
            // Get the VideoView from the layout
            VideoView videoView = findViewById(R.id.videoView);

            // Construct the video URL
            String baseUrl = "http://10.0.2.2:8080/media/actualMovies/";
            String videoUrl = baseUrl + movie.getPathToMovie(); // Replace with your dynamic URL

            // Set up the VideoView
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setVideoURI(videoUri);

            // Add MediaController for playback controls (optional)
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            // Start the video playback
            videoView.setOnPreparedListener(mp -> videoView.start());

            // Handle errors (optional)
            videoView.setOnErrorListener((mp, what, extra) -> {
                // Log or show error message
                return true;
            });
            //more fields
        });
        currentMovieViewModel.fetchCurrentMovie(movieId);


        //worked before:
        /*
        currentMovieViewModel = new CurrentMovieViewModel(this, this);


        String movieId = getIntent().getStringExtra("movie_id");
        currentMovieViewModel.getCurrentMovie().observe(this, movie->{
            //init fields of movie
            TextView currentID = findViewById(R.id.currentID);
            currentID.setText(movie.get_id());
            //more fields
        });
        currentMovieViewModel.fetchCurrentMovie(movieId);
         */

    }
}