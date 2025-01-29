package com.example.catflix_android.ViewModels;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.MovieRepository;
import com.example.catflix_android.Repositories.UserRepository;

public class MovieViewModel extends ViewModel {
    private final Context context;

    private MovieRepository repository;

    public MovieViewModel(Context context, LifecycleOwner owner){

        this.context = context;
        this.repository = new MovieRepository(context,owner);
    }

    public void getMovies(MutableLiveData<MoviesResponse> moviesResponse){
        this.repository.getMovies(moviesResponse);
    }


}

