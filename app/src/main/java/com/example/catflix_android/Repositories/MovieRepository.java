package com.example.catflix_android.Repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.MovieAPI;
import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.User;

public class MovieRepository {
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
    }

    public void getMovies(MutableLiveData<MoviesResponse> moviesResponse){
        this.api.getMovies(moviesResponse,context);
    }

}
