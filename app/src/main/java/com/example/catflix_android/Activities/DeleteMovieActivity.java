package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CurrentMovieViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteMovieActivity extends AppCompatActivity {

    Map<String, String> movieMap = new HashMap<>();
    private Spinner movieDropdown;
    private CurrentMovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_movie);

        // Initialize the Spinner
        movieDropdown = findViewById(R.id.movieDropdown);

        Button deleteMovie = findViewById(R.id.deleteSelectedMovieBTN);
        Button editMovie = findViewById(R.id.editSelectedMovieBtn);

        // Set click listener for editing a movie
        editMovie.setOnClickListener(v -> {
            Intent intent = new Intent(DeleteMovieActivity.this, UpdateMovieActivity.class);
            intent.putExtra("movie_id", movieMap.get((String) movieDropdown.getSelectedItem()));
            intent.putExtra("movie_name", (String) movieDropdown.getSelectedItem());
            startActivity(intent);
        });

        // Set click listener for deleting a movie
        deleteMovie.setOnClickListener(v -> deleteSelectedMovie(movieMap.get((String) movieDropdown.getSelectedItem())));

        // Initialize the ViewModel
        movieViewModel = new CurrentMovieViewModel(this, this);

        // Observe the movie list and populate the dropdown
        movieViewModel.getAllMovies().observe(this, this::populateDropdown);

        // Add the header fragment dynamically
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment);
            transaction.commit();
        }

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch movies from ViewModel
        movieViewModel.getAllMovies();
    }

    private void populateDropdown(List<Movie> movies) {
        // Extract movie titles from Movie objects
        List<String> movieTitles = new ArrayList<>();
        for (Movie movie : movies) {
            movieTitles.add(movie.getName());
            movieMap.put(movie.getName(), movie.get_id());
        }

        // Create an ArrayAdapter for the Spinner with custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_layout,  // Use your custom layout for the dropdown items
                movieTitles
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_layout); // Set dropdown view to use the same custom layout

        // Set the adapter to the Spinner
        movieDropdown.setAdapter(adapter);

        // Set an item selected listener for the Spinner
        movieDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMovie = movieMap.get(movieTitles.get(position));
                Toast.makeText(DeleteMovieActivity.this, "Selected: " + selectedMovie, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no selection is made
            }
        });
    }

    private void deleteSelectedMovie(String movieId) {
        // Use the ViewModel to delete the movie
        this.movieViewModel.deleteMovie(movieId);
    }
}