package com.example.catflix_android.ViewModels;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.MovieRepository;
import com.example.catflix_android.Repositories.UserRepository;

public class MovieViewModel extends ViewModel {
    private final Context context;
    MutableLiveData<Movie> uploadedMovie;


    private MovieRepository repository;
    private LifecycleOwner owner;

    public MovieViewModel(Context context, LifecycleOwner owner){

        this.context = context;
        this.owner = owner;
        this.repository = new MovieRepository(context,owner);
    }

    public void getMovies(MutableLiveData<MoviesResponse> moviesResponse){
        this.repository.getMovies(moviesResponse);
    }

    public MutableLiveData<Movie> getUploadedMovie() {
        if(uploadedMovie == null){uploadedMovie = new MutableLiveData<>();}
        return uploadedMovie;
    }
    public void uploadMovie(Movie movieCreate) {
        this.repository.getUploadedMovie().observe(this.owner, movie -> {
            if(movie!=null)
            {
                this.uploadedMovie.setValue(movie);
            }
        });
        this.repository.uploadMovie(movieCreate);
    }
    public void editMovie(Movie movieUpdate) {
        this.repository.editMovie(movieUpdate);
    }
}

