package com.example.catflix_android.DataTypes;

import com.example.catflix_android.Entities.Movie;
import java.util.List;

public class CategoryHelper {
    private String categoryName;
    private List<Movie> movies;

    public CategoryHelper(String categoryName, List<Movie> movies) {
        this.categoryName = categoryName;
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "CategoryHelper{" +
                "CategoryName='" + categoryName + '\'' +
                ", movies=" + movies +
                '}';
    }

    public String getName(){
        return this.categoryName;
    }
    public List<Movie> getMovies(){
        return this.movies;
    }
}
