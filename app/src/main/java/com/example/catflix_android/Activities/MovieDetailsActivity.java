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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catflix_android.Adapters.MovieAdapter;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CurrentMovieViewModel;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    private CurrentMovieViewModel currentMovieViewModel;
    private MovieAdapter movieAdapter;

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

        // Initialize ViewModel
        currentMovieViewModel = new CurrentMovieViewModel(this, this);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.movieList);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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
                Intent intent = new Intent(MovieDetailsActivity.this, WatchMovieActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            });
        });

        // Fetch movie details
        currentMovieViewModel.fetchCurrentMovie(movieId);

        // Fetch and observe recommendations
        currentMovieViewModel.getCurrentRecommendation().observe(this, recommendedMovies -> {
            if (recommendedMovies != null && !recommendedMovies.isEmpty()) {
                movieAdapter.setMovieResponse(recommendedMovies);
            }
        });
        currentMovieViewModel.getCppRecommendation(movieId);
    }
}
