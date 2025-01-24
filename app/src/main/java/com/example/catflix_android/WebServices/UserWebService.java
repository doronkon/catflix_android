package com.example.catflix_android.WebServices;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.Entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface  UserWebService {
    @POST("tokens")
    public Call<LoginResponse> login(@Body LoginUser loginUser);

    @POST("users")
    public Call<User> signUp(@Body User userCreate);
    @GET("users/{id}")
    public Call<User> getUser(@Header("user") String token, @Path("id") String id);
    @GET("users/index/all")
    public Call<List<User>> index();
}
