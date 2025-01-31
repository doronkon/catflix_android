package com.example.catflix_android.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catflix_android.Adapters.MovieAdapter;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.Repositories.MovieRepository;
import com.example.catflix_android.ViewModels.SearchViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;

    private RecyclerView returnedMoviesRecyclerView;
    private MovieAdapter movieAdapter;
    private EditText searchInput;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        // Adjust insets for Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }

        // Initialize UI components
        searchInput = findViewById(R.id.searchInput);

        // Set up RecyclerView
        // Initialize alreadyWatched RecyclerView
        returnedMoviesRecyclerView = findViewById(R.id.movieList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(false);  // Ensures scrolling starts naturally from the top
        returnedMoviesRecyclerView.setLayoutManager(layoutManager);

// Remove SnapHelper for smooth scrolling (optional)
        returnedMoviesRecyclerView.setNestedScrollingEnabled(true);

        // Initialize alreadyWatched Adapter
        movieAdapter = new MovieAdapter(this);
        returnedMoviesRecyclerView.setAdapter(movieAdapter);


        searchViewModel = new SearchViewModel(this, this);

        // Observe search results
        searchViewModel.getSearchResults().observe(this, movies -> {
            if (movies != null) {
                movieAdapter.setMovieResponse(movies);
            } else {
                // Handle null response
                System.out.println("Search results are null");
            }
        });

        // Set up search input listener
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Trigger search when text changes
                searchViewModel.searchMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}