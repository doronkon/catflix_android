package com.example.catflix_android.WebServices;

import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Entities.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryWebService {
    @GET("categories")
    public Call<List<Category>> getCategories(@Header("user") String userToken);

    @DELETE("categories/{id}")
    public Call<Void> deleteCategory(@Header("user") String userToken, @Path("id") String id);

    @PATCH("categories/{id}")
    public Call<Void> editCategory(@Header("user") String userToken, @Path("id") String id, @Body Category cat);

    @POST("categories")
    public Call<Void> createCategory(@Header("user") String userToken, @Body Category cat);

    //http://localhost:8080/api/categories/movies/${id}
    @GET("categories/movies/{id}")
    public Call<List<Movie>> fetchCategoryMovies(@Header("user") String userToken, @Path("id") String id);
}
