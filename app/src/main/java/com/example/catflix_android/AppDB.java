package com.example.catflix_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.catflix_android.Daos.CategoryDao;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.Daos.UserDao;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;

@Database(entities = {User.class, Movie.class, Category.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract MovieDao movieDao();
}
