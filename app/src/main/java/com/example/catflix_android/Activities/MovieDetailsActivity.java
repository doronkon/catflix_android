package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CurrentMovieViewModel;
import com.example.catflix_android.Entities.Movie;

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

        // Initialize the ViewModel
        currentMovieViewModel = new CurrentMovieViewModel(this, this);

        // Get movie ID from Intent
        String movieId = getIntent().getStringExtra("movie_id");

        // Observe movie details
        currentMovieViewModel.getCurrentMovie().observe(this, movie -> {
            // Initialize movie fields
            TextView movieName = findViewById(R.id.movieName);
            movieName.setText(movie.getName());

            TextView movieDescription = findViewById(R.id.movieDescription);
            movieDescription.setText(movie.getDescription());

            ImageView imageView = findViewById(R.id.movieThumbnail);
            String imageUrl = "http://10.0.2.2:8080/media/movieThumbnails/" + movie.getThumbnail();


            Glide.with(MovieDetailsActivity.this)
                    .load(imageUrl)
                    .into(imageView);

            // Initialize the "Watch Movie" button
            Button watchMovieButton = findViewById(R.id.watchMovieButton);
            watchMovieButton.setOnClickListener(v -> {
                currentMovieViewModel.patchMovieForUser();
                // Create an intent to navigate to WatchMovieActivity
                Intent intent = new Intent(MovieDetailsActivity.this, WatchMovieActivity.class);

                // Pass the entire Movie object
                intent.putExtra("movie", movie);

                // Start WatchMovieActivity
                startActivity(intent);
            });
        });

        // Fetch movie details
        currentMovieViewModel.fetchCurrentMovie(movieId);
    }
}
