package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.MovieViewModel;
import com.example.catflix_android.ViewModels.UserViewModel;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         TextView textBoxMovies = findViewById(R.id.returnedMovies);

        MovieViewModel model = new MovieViewModel(this, this);
        MutableLiveData<MoviesResponse> movieResponse = new MutableLiveData<>();
        movieResponse.observe(this, new Observer<MoviesResponse>() {
            @Override
            public void onChanged(MoviesResponse returnedMovies) {
                if (returnedMovies != null) {
                    System.out.println("Kill me");
                    textBoxMovies.setText(returnedMovies.toString());
                } else {
                    System.out.println("ima shelha");
                }
            }
        });
        model.getMovies(movieResponse);


    }
}