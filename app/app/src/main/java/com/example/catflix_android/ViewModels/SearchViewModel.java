package com.example.catflix_android.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.DataTypes.Movie;
import com.example.catflix_android.Repositories.MovieRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();
    private MovieRepository movieRepository;

    public SearchViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MutableLiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public void searchMovies(String query) {
        movieRepository.searchMovies(query, searchResults);
    }
}