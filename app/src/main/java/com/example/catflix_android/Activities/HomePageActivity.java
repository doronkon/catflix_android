package com.example.catflix_android.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catflix_android.Adapters.CategoryAdapter;
import com.example.catflix_android.Adapters.MovieAdapter;
import com.example.catflix_android.DataTypes.CategoryHelper;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.MovieViewModel;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;


import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "isDarkMode";
    private RecyclerView returnedMoviesRecyclerView;
    private MovieAdapter movieAdapter;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", true);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        // Add the fragment dynamically to the container (header_container)
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }

        // Initialize alreadyWatched RecyclerView
        returnedMoviesRecyclerView = findViewById(R.id.movieList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        returnedMoviesRecyclerView.setLayoutManager(layoutManager);

        // Optionally add a snap effect to the alreadyWatched RecyclerView
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(returnedMoviesRecyclerView);

        // Initialize alreadyWatched Adapter
        movieAdapter = new MovieAdapter(this);
        returnedMoviesRecyclerView.setAdapter(movieAdapter);

        // Initialize the RecyclerView for promotedMovies
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel and observe MoviesResponse
        MovieViewModel model = new MovieViewModel(this, this);
        MutableLiveData<MoviesResponse> movieResponse = new MutableLiveData<>();
        movieResponse.observe(this, new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse returnedMovies) {
                if (returnedMovies != null) {
                    // Handle alreadyWatched movies
                    List<Movie> alreadyWatchedMovies = returnedMovies.getAlreadyWatched();
                    movieAdapter.setMovieResponse(alreadyWatchedMovies);

                    // Handle promotedMovies categories
                    List<CategoryHelper> promotedMovies = returnedMovies.getPromotedMovies();
                    categoryAdapter = new CategoryAdapter(HomePageActivity.this, promotedMovies);
                    categoryRecyclerView.setAdapter(categoryAdapter);

                    // Get the first promoted movie and play the video
                    if (promotedMovies != null && !promotedMovies.isEmpty()) {
                        CategoryHelper firstCategory = promotedMovies.get(0);
                        List<Movie> moviesInCategory = firstCategory.getMovies();
                        if (moviesInCategory != null && !moviesInCategory.isEmpty()) {
                            Movie firstMovie = moviesInCategory.get(0);
                            TextView videoBannerText = findViewById(R.id.bannerText);
                            Button playBannerButton = findViewById(R.id.playButton);
                            videoBannerText.setText(firstMovie.getName());

                            // Set the OnClickListener
                            playBannerButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(HomePageActivity.this, MovieDetailsActivity.class);
                                     intent.putExtra("movie_id", firstMovie.get_id());
                                    startActivity(intent);
                                }
                            });


                            // Assuming you have a video URL for the first movie
                            String videoUrl = "http://10.0.2.2:8080/media/actualMovies/" + firstMovie.getPathToMovie();

                            VideoView videoBanner = findViewById(R.id.videoBanner);
                            Uri uri = Uri.parse(videoUrl);
                            videoBanner.setVideoURI(uri);
                            videoBanner.start();
                        }
                    }
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
