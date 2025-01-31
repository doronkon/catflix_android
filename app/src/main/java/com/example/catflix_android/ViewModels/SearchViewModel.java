package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Repositories.MovieRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();
    private final LifecycleOwner owner;
    private MovieRepository movieRepository;

    public SearchViewModel(Context context, LifecycleOwner owner) {
        this.owner = owner;

        this.movieRepository = new MovieRepository(context, owner);
    }

    public MutableLiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public void searchMovies(String query) {
        this.movieRepository.getSearchMovies().observe(this.owner, returnedMovies->{
            if (returnedMovies != null) {
                this.searchResults.setValue(returnedMovies);
            }
         });
        movieRepository.fetchSearchMovies(query);
    }
}