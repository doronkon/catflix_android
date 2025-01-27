package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Repositories.CategoryRepository;
import com.example.catflix_android.Repositories.MovieRepository;

import java.util.Arrays;
import java.util.List;

import kotlinx.coroutines.CoroutineScope;

public class CategoryViewModel extends ViewModel {

    private final Context context;
    private final LifecycleOwner owner;
    private final CategoryRepository repository;
    private MutableLiveData<List<Category>> categories;

    public CategoryViewModel(Context context, LifecycleOwner owner) {
        this.context = context;
        this.owner = owner;
        this.categories = new MutableLiveData<>();
        this.repository = new CategoryRepository(context,owner);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void fetchCategories() {
        // Simulated fetch
        this.repository.getCategories().observe(this.owner,returnedCategories->{
            this.categories.setValue(returnedCategories);
        });
        this.repository.fetchCategories();
    }
}

