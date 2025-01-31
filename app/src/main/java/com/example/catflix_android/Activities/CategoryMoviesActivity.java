package com.example.catflix_android.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catflix_android.Adapters.MovieAdapter;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CategoryViewModel;

public class CategoryMoviesActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;
    private RecyclerView returnedMoviesRecyclerView;
    private MovieAdapter movieAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_movies);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Add the fragment dynamically to the container (header_container)
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }

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


        String categoryId = getIntent().getStringExtra("category_id");
        String categoryName = getIntent().getStringExtra("category_name");
        TextView textViewEditingMovie = findViewById(R.id.textViewCategoryMovie);
        textViewEditingMovie.setText("You are watching: " + categoryName);

        categoryViewModel = new CategoryViewModel(this,this);
        //observer here filling the adapter with movies
        categoryViewModel.getCategoryMovies().observe(this, returnedMovies->{
            //doron help adapter
            if(returnedMovies != null)
            {
                movieAdapter.setMovieResponse(returnedMovies);
            }
        });

        categoryViewModel.fetchCategoryMovies(categoryId);




    }
}