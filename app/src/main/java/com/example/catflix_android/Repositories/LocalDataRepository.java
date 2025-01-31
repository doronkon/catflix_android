package com.example.catflix_android.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.MovieAPI;
import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.Daos.UserDao;
import com.example.catflix_android.DataManager;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalDataRepository {
    private final MovieDao daoMovie;
    private final MovieAPI apiMovie;
    private final UserDao daoUser;
    private final UserAPI apiUser;
    private final Context context;
    MutableLiveData<List<User>> users;
    MutableLiveData<List<Movie>> movies;

    private final LifecycleOwner owner;

    public LocalDataRepository(Context context, LifecycleOwner owner) {
        this.users = new MutableLiveData<>();
        this.movies = new MutableLiveData<>();
        this.context = context;
        apiMovie = new MovieAPI();
        apiUser = new UserAPI();
        AppDB database = AppDB.getInstance(context);
        daoMovie = database.movieDao();
        daoUser = database.userDao();
        this.owner = owner;
    }

    //        if (DataManager.getIntialized()) {
//            initialized.setValue(true);
//            return;
//        }
    public void init(MutableLiveData<Boolean> initComplete) {
        final int totalOperations = 2;  // There are 2 operations: inserting users and movies
        final AtomicInteger completedOperations = new AtomicInteger(0);  // AtomicInteger for thread-safety


        Thread deleteThread = new Thread(() -> {this.daoUser.deleteAll();this.daoMovie.deleteAll();});
// Wait for the delete thread to finish without blocking the main thread.
        deleteThread.start();
        try {
                deleteThread.join();
        } catch (Exception ex) {
            Log.w("THREAD ERROR", ex);
            Thread.currentThread().interrupt();
        }
        this.apiUser.index(this.users, context);
        this.apiMovie.index(this.movies, context);

        this.users.observe(owner, usersResponse -> {
            if (usersResponse != null) {
                User[] userArray = usersResponse.toArray(new User[0]); // Convert List to Array
                Thread insertUsersThread = new Thread(() -> daoUser.insert(userArray));
                insertUsersThread.start();
                try {
                    insertUsersThread.join();
                    if (completedOperations.incrementAndGet() == totalOperations) {
                        initComplete.postValue(true); // Notify ViewModel
                    }
                }catch (Exception ex) {
                    Log.w("THREAD ERROR", ex);
                    Thread.currentThread().interrupt();}

            } else {
                System.out.println("not worked");
                // Handle error or failure case
            }
        });
        this.movies.observe(owner, moviesResponse -> {
            if (moviesResponse != null) {
                Movie[] movieArray = moviesResponse.toArray(new Movie[0]); // Convert List to Array
                Thread insertMoviesThread = new Thread(() -> daoMovie.insert(movieArray));
                insertMoviesThread.start();
                try {
                    insertMoviesThread.join();
                    if (completedOperations.incrementAndGet() == totalOperations) {
                        initComplete.postValue(true); // Notify ViewModel
                    }
                }catch (Exception ex)
                {
                    Log.w("THREAD ERROR", ex);
                    Thread.currentThread().interrupt();}


            } else {
                System.out.println("not worked");
                // Handle error or failure case
            }
        });

    }
    public void deleteRoom(MutableLiveData<Boolean> deleteComplete) {
        Thread deleteThread = new Thread(() -> {this.daoUser.deleteAll();this.daoMovie.deleteAll();});
// Wait for the delete thread to finish without blocking the main thread.
        deleteThread.start();
        try {
            deleteThread.join();
            DataManager.reset();
            deleteComplete.setValue(true);
        } catch (Exception ex) {
            Log.w("THREAD ERROR", ex);
            Thread.currentThread().interrupt();
        }

    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

}
