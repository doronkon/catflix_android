package com.example.catflix_android.WebServices;

import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CategoryWebService {
    @GET("categories")
    public Call<List<Category>> getCategories(@Header("user") String userToken);

    @DELETE("categories/{id}")
    public Call<Void> deleteCategory(@Header("user") String userToken, @Path("id") String id);
}
