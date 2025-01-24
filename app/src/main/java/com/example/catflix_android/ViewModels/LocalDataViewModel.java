package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.LocalDataRepository;
import com.example.catflix_android.Repositories.MovieRepository;

import java.util.List;

public class LocalDataViewModel {
    MutableLiveData<List<User>> users;
    MutableLiveData<List<Movie>> movies;
    MutableLiveData<Boolean> finished;
    private Context context;

    private LocalDataRepository repository;

    public LocalDataViewModel(Context context, LifecycleOwner owner){

        this.context = context;
        this.repository = new LocalDataRepository(context,owner);
        this.movies  = this.repository.getMovies();
        this.users = repository.getUsers();
    }
    public void init(){
        this.repository.init();
    }

}
