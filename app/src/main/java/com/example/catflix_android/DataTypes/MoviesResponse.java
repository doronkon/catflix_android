package com.example.catflix_android.DataTypes;

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

//    public List<Movie> getCategoryMovies(String name){
//        for(int i = 0; i < promotedMovies.size(); i++){
//            CategoryHelper current = promotedMovies.get(i);
//            if (Objects.equals(current.getName(), name)){
//                return current.getMovies();
//            }
//        }
//        return null;
//    }


    public List<CategoryHelper> getPromotedMovies() {
        return promotedMovies;
    }

    public List<Movie> getAlreadyWatched(){
        return this.alreadyWatched;
    }
}
