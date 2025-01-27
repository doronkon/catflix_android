package com.example.catflix_android.Repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.CategoryAPI;
import com.example.catflix_android.Entities.Category;

import java.util.List;


public class CategoryRepository {
    private final LifecycleOwner owner;
    private final Context context;
    private final CategoryAPI api;
    private MutableLiveData<List<Category>> categories;

    public CategoryRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        this.owner = owner;
        this.categories = new MutableLiveData<>();
        this.api = new CategoryAPI();
    }
    public void fetchCategories() {
        this.api.fetchCategories(this.categories,context);
    }

    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }
}
