package com.example.catflix_android.DataTypes;

import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Entities.Movie;

import java.util.List;

public class MoviesResponse {
    private List <CategoryHelper> promotedMovies;
    private List<Movie> alreadyWatched;

    public MoviesResponse(List<CategoryHelper> promotedMovies, List<Movie> alreadyWatched) {
        this.promotedMovies = promotedMovies;
        this.alreadyWatched = alreadyWatched;
    }

    @Override
    public String toString() {
        return "MoviesResponse{" +
                "promotedMovies=" + promotedMovies +
                ", alreadyWatched=" + alreadyWatched +
                '}';
    }
}
