package com.example.catflix_android.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catflix_android.Adapters.MovieAdapter;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.MovieViewModel;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private RecyclerView returnedMoviesRecyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Adjust insets for Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        returnedMoviesRecyclerView = findViewById(R.id.movieList);

        // Set the layout manager to horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        returnedMoviesRecyclerView.setLayoutManager(layoutManager);

        // Optionally add a snap effect to the RecyclerView
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(returnedMoviesRecyclerView);

        // Initialize Adapter
        movieAdapter = new MovieAdapter(this);

        // Set adapter to RecyclerView
        returnedMoviesRecyclerView.setAdapter(movieAdapter);

        // Initialize ViewModel and observe MoviesResponse
        MovieViewModel model = new MovieViewModel(this, this);
        MutableLiveData<MoviesResponse> movieResponse = new MutableLiveData<>();
        movieResponse.observe(this, new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse returnedMovies) {
                if (returnedMovies != null) {
                    // Extract alreadyWatched movies
                    List<Movie> alreadyWatchedMovies = returnedMovies.getAlreadyWatched();

                    // Update adapter with the alreadyWatched movies
                    movieAdapter.setMovieResponse(alreadyWatchedMovies);

                } else {
                    // Log or handle null response appropriately
                    System.out.println("Movies response is null");
                }
            }
        });

        // Trigger the HTTP request to fetch movies
        model.getMovies(movieResponse);
    }

}
