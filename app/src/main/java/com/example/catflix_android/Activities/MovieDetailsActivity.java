package com.example.catflix_android.Activities;

import android.os.Bundle;
import android.widget.TextView;

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
        currentMovieViewModel = new CurrentMovieViewModel(this, this);


        String movieId = getIntent().getStringExtra("movie_id");
        currentMovieViewModel.getCurrentMovie().observe(this, movie->{
            //init fields of movie
            TextView currentID = findViewById(R.id.currentID);
            currentID.setText(movie.get_id());
            //more fields
        });
        currentMovieViewModel.fetchCurrentMovie(movieId);

    }
}