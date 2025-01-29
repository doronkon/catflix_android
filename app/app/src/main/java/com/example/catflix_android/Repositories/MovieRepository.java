package com.example.catflix_android.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.MovieAPI;
import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;

public class MovieRepository {
    private MutableLiveData<Movie> currentMovie;

    private MovieDao dao;
    private MovieAPI api;
    private Context context;

    private LifecycleOwner owner;

    public MovieRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        api = new MovieAPI();
        AppDB database = AppDB.getInstance(context);
        dao = database.movieDao();
        this.owner = owner;
        currentMovie = new MutableLiveData<>();
    }

    public void getMovies(MutableLiveData<MoviesResponse> moviesResponse){
        this.api.getMovies(moviesResponse,context);
    }

    public void searchMovies(String query, MutableLiveData<List<Movie>> searchResults) {
        // Simulate fetching data from an API or database
        new Thread(() -> {
            List<Movie> movies = dao.searchMovies(query); // Assuming you have a search method in your DAO
            searchResults.postValue(movies);
        }).start();
    }

    public void fetchCurrentMovie(String MovieID) {
        //gets the user and posts it in MTLD- mutable live data
        new Thread(() -> this.currentMovie.postValue(this.dao.get(MovieID))).start();
    }
    public MutableLiveData<Movie> getCurrentMovie()
    {
        return this.currentMovie;
    }

}
