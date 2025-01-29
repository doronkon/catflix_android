package com.example.catflix_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.catflix_android.Daos.CategoryDao;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.Daos.UserDao;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;

@Database(entities = {User.class, Movie.class, Category.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    private static volatile AppDB instance;
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract MovieDao movieDao();

    // Singleton method to get the database instance
    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDB.class,
                            "LocalData"
                    )
                    .fallbackToDestructiveMigration() // Replace with migrations for production
                    .build();
        }
        return instance;
    }
}
