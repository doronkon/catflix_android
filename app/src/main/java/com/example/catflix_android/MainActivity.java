package com.example.catflix_android;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.Entities.Movie;

public class MainActivity extends AppCompatActivity {

    private AppDB db;
    private MovieDao movieDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDB.class,
                "MoviesDB")
                .allowMainThreadQueries()
                .build();
        movieDao = db.movieDao();
        Movie newMovie = new Movie("132","hazan","18",false,"hazan desc","hazna path","kaki",
                "hazan","hazan","hazan categpry","today","hazan name");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}