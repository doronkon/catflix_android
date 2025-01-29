package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.MovieRepository;
import com.example.catflix_android.Repositories.UserRepository;

public class CurrentMovieViewModel extends ViewModel {
    MutableLiveData<Movie> currentMovie;
    private final MovieRepository repository;
    private LifecycleOwner owner;

    public CurrentMovieViewModel(Context context, LifecycleOwner owner){
        this.repository = new MovieRepository(context,owner);
        this.owner = owner;
    }

    public LiveData<Movie> getCurrentMovie() {
        if(currentMovie == null)
        {
            currentMovie = new MutableLiveData<>();
        }
        return currentMovie;
    }
    public void fetchCurrentMovie(String movieID)
    {
        this.repository.getCurrentMovie().observe(owner,movie -> {
            this.currentMovie.setValue(movie);
        });
       this.repository.fetchCurrentMovie(movieID);

    }
}
