package com.example.catflix_android.WebServices;

import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CategoryWebService {
    @GET("categories")
    public Call<List<Category>> getCategories(@Header("user") String userToken);
}
