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

import java.util.List;

public class CurrentMovieViewModel extends ViewModel {
    MutableLiveData<Movie> currentMovie;

    MutableLiveData<List<Movie>> allMovies;
    MutableLiveData <List<Movie>> currentRecommendation;
    private final MovieRepository repository;
    private LifecycleOwner owner;

    public CurrentMovieViewModel(Context context, LifecycleOwner owner){
        this.repository = new MovieRepository(context,owner);
        this.owner = owner;
        this.allMovies = new MutableLiveData<>();
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

    public void patchMovieForUser(){
        this.repository.patchMovieForUser();
    }

    public MutableLiveData<List<Movie>> getCurrentRecommendation() {
        if(currentRecommendation == null){
            currentRecommendation = new MutableLiveData<>();
        }
        return currentRecommendation;
    }

    public void getCppRecommendation(String movieID){
        this.repository.getCppRecommendation(movieID);
        this.repository.getCurrentRecommendation().observe(owner,recommendList -> {
            this.currentRecommendation.setValue(recommendList);
        });
    }

    // Get all movies as LiveData
    public LiveData<List<Movie>> getAllMovies() {
        // If data is already fetched, return it
        if (allMovies.getValue() == null) {
            fetchAllMovies(); // Fetch movies if not already fetched
        }
        return allMovies;
    }

    // Fetch movies and update the LiveData
    private void fetchAllMovies() {
        // Observe LiveData from the repository and update this ViewModel's LiveData
        repository.getAllMovies().observeForever(movies -> {
            allMovies.setValue(movies);  // Update LiveData in ViewModel when data is fetched
        });
    }

    public void deleteMovie(String movieId){
        this.repository.deleteMovie(movieId);
    }
}
