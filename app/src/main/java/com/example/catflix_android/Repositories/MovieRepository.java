package com.example.catflix_android.Repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.MovieAPI;
import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.DataTypes.StringMovie;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;

import java.util.List;

public class MovieRepository {
    MutableLiveData<Boolean> finishedMongoPatch;

    private MutableLiveData<List<Movie>> searchMovies;
    private MutableLiveData<Movie> currentMovie;
    private MutableLiveData<Boolean> finishedUpdate;


    private MutableLiveData<List<Movie>> currentRecommendation;

    private MutableLiveData<Boolean> flag = new MutableLiveData<>();

    private MutableLiveData<List<Movie>> allMovies;
    private MovieDao dao;
    private MovieAPI api;
    private Context context;

    private LifecycleOwner owner;
    private MutableLiveData<Movie> uploadedMovie;
    private MutableLiveData<Movie> uploadedMovieAPI;


    public MovieRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        api = new MovieAPI();
        AppDB database = AppDB.getInstance(context);
        dao = database.movieDao();
        this.owner = owner;
        currentMovie = new MutableLiveData<>();
        uploadedMovieAPI = new MutableLiveData<>();
        currentRecommendation = new MutableLiveData<>();
        finishedUpdate = new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getCurrentRecommendation() {
        return currentRecommendation;
    }

    public void getMovies(MutableLiveData<MoviesResponse> moviesResponse){
        this.api.getMovies(moviesResponse,context);
    }

    public void fetchCurrentMovie(String MovieID) {
        //gets the user and posts it in MTLD- mutable live data
        new Thread(() -> this.currentMovie.postValue(this.dao.get(MovieID))).start();
    }
    public MutableLiveData<Movie> getCurrentMovie()
    {
        return this.currentMovie;
    }

    public MutableLiveData<Movie> getUploadedMovie() {
        if(this.uploadedMovie == null){uploadedMovie = new MutableLiveData<>();}
        return uploadedMovie;
    }
    public void uploadMovie(Movie movieCreate) {
        uploadedMovieAPI.setValue(movieCreate);
        //we update api and then update room
        this.uploadedMovieAPI.observe(owner, movie -> {
            if (movie != null && movie.get_id() != null) {
                Thread addDaoMovie = new Thread(() -> dao.insert(movie));
                addDaoMovie.start();
                try{
                    addDaoMovie.join();
                } catch (Exception ex)
                {
                    Log.w("THREAD ERROR", ex);
                    Thread.currentThread().interrupt();}
                this.uploadedMovie.setValue(movie);

            }
        });
        this.api.uploadVideo(movieCreate,uploadedMovieAPI,this.context);

    }


    public void patchMovieForUser() {
        StringMovie baruchHashem = new StringMovie(this.currentMovie.getValue().get_id());
        this.finishedMongoPatch = new MutableLiveData<>();
        this.finishedMongoPatch.observe(this.owner, val ->{
            if(val)
            {
                this.api.patchInCpp(this.currentMovie.getValue().get_id(),this.context);
            }
        });
        this.api.patchInMongo(this.finishedMongoPatch,baruchHashem,this.context);
    }

    public void getCppRecommendation(String movieId){
        this.api.getCppRecommendation(movieId, this.context, currentRecommendation);
    }
    // Get all movies as LiveData
    public LiveData<List<Movie>> getAllMovies() {
        MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();

        // Fetch the movies on a background thread to avoid blocking the UI thread
        new Thread(() -> {
            List<Movie> movies = dao.index(); // Fetch movies from DAO
            moviesLiveData.postValue(movies);  // Update LiveData on the main thread
        }).start();

        return moviesLiveData;  // Return LiveData that can be observed by ViewModel
    }

    public void deleteMovie(String movieId){
        this.flag = new MutableLiveData<>();
        this.flag.observe(this.owner, returnedFlag->{
            if(returnedFlag){
                // delete from movie dao
                new Thread (() -> {
                    dao.deleteMovieById(movieId);
                }).start();
            }
        });
        this.api.deleteMovie(movieId, this.context,flag);

    }
    public void editMovie(Movie movieUpdate) {
        this.finishedUpdate = new MutableLiveData<>(); // Reset LiveData

        this.finishedUpdate.observe(this.owner,val->{
            if(val)
            {
                Thread editDao = new Thread (() -> dao.update(movieUpdate));
                editDao.start();
                try{
                    editDao.join();
                    Toast.makeText(this.context, "Edited successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Log.w("THREAD ERROR", ex);
                    Thread.currentThread().interrupt();
                }
            }else {
                Toast.makeText(this.context, "Edit failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
            }
        });
        this.api.editMovie(movieUpdate,this.finishedUpdate);
    }

    public LiveData<List<Movie>> getSearchMovies()
    {
        if(searchMovies == null)
        {
            searchMovies = new MutableLiveData<>();
        }
        return this.searchMovies;
    }
    public void fetchSearchMovies(String query) {
        this.api.fetchSearchMovies(this.searchMovies, query);
    }

}
