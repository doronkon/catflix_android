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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catflix_android.Adapters.MovieAdapter;
import com.example.catflix_android.DataTypes.Movie;
import com.example.catflix_android.R;
import com.example.catflix_android.Repositories.MovieRepository;
import com.example.catflix_android.ViewModels.SearchViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;
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

        // Initialize UI components
        searchInput = findViewById(R.id.searchInput);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        // Set up RecyclerView
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(this);
        searchResultsRecyclerView.setAdapter(movieAdapter);

        // Initialize ViewModel
        MovieRepository movieRepository = new MovieRepository(this, this);
        searchViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) new SearchViewModel(movieRepository);
            }
        }).get(SearchViewModel.class);

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