package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.LocalDataRepository;
import com.example.catflix_android.Repositories.MovieRepository;

import java.util.List;

public class LocalDataViewModel {
    MutableLiveData<List<User>> users;
    MutableLiveData<List<Movie>> movies;
    private MutableLiveData<Boolean> initComplete;
    private Context context;

    private LocalDataRepository repository;
    private LifecycleOwner owner;

    public LocalDataViewModel(Context context, LifecycleOwner owner){
        this.owner = owner;
        this.context = context;
        this.repository = new LocalDataRepository(context,owner);
        this.movies  = this.repository.getMovies();
        this.users = repository.getUsers();
    }
    public void init(){
        this.repository.init(initComplete);
    }
    public LiveData<Boolean> getInitComplete() {
        if(initComplete == null)
        {
            initComplete = new MutableLiveData<>();
        }
        return initComplete;
    }

}
