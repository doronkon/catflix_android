package com.example.catflix_android.Repositories;

import android.content.Context;
import com.example.catflix_android.Entities.Movie;
import android.util.Log;
import java.util.List;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.CategoryAPI;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Daos.MovieDao;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.APIs.MovieAPI;



public class CategoryRepository {
    private final LifecycleOwner owner;
    private final Context context;
    private final CategoryAPI api;
    private MutableLiveData<Boolean> flag = new MutableLiveData<>();
    private MutableLiveData<Boolean> completedDao = new MutableLiveData<>();

    MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

    private final MovieAPI apiMovie;
    private MovieDao daoMovie;
    private MutableLiveData<List<Category>> categories;

    public CategoryRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        this.owner = owner;
        this.categories = new MutableLiveData<>();
        this.api = new CategoryAPI();
        this.apiMovie = new MovieAPI();
        AppDB database = AppDB.getInstance(context);
        daoMovie = database.movieDao();

    }
    public void fetchCategories() {
        this.api.fetchCategories(this.categories,context);
    }
    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }

    public void deleteCategory(String categoryId) {
        this.flag.observe(this.owner, returnedFlag -> {
            if (returnedFlag) {
                new Thread(() -> {
                    daoMovie.deleteAll();
                    completedDao.postValue(true);
                }).start();
            }
        });

        this.completedDao.observe(this.owner, returnedFlag -> {
            completedDao.postValue(false);
            MutableLiveData<Boolean> hazan = new MutableLiveData<>();
            if (returnedFlag) {
                initMovies(hazan);
            }
        });
        this.api.deleteCategory(categoryId, this.context, flag);
    }


    public void initMovies(MutableLiveData<Boolean> initComplete) {

        this.apiMovie.index(this.movies, context);

        this.movies.observe(owner, moviesResponse -> {
            if (moviesResponse != null) {
                Movie[] movieArray = moviesResponse.toArray(new Movie[0]); // Convert List to Array
                Thread insertMoviesThread = new Thread(() -> daoMovie.insert(movieArray));
                insertMoviesThread.start();
                try {
                    insertMoviesThread.join();
                    initComplete.postValue(true); // Notify ViewModel
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

}
