package com.example.catflix_android.WebServices;

import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MovieWebService {

    @GET("movies")
    public Call<MoviesResponse> getMovies(@Header("user") String userToken);
    @GET("movies/index/all")
    public Call<List<Movie>> index();

}
