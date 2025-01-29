package com.example.catflix_android.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.catflix_android.Entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    List<Movie> index();


    @Query("SELECT * FROM movies WHERE _id=:id")
    Movie get(String id);


    @Query("SELECT * FROM movies WHERE title LIKE :query")
    List<Movie> searchMovies(String query);

    @Insert
    void insert(Movie...movies);

    @Delete
    void delete(Movie...movies);
    @Query("DELETE from movies")
    void deleteAll();

    @Update
    void update(Movie...movies);
}