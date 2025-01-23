package com.example.catflix_android.WebServices;
import com.example.catflix_android.Entities.LoginResponse;
import com.example.catflix_android.Entities.LoginUser;
import com.example.catflix_android.Entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface  UserWebService {
    @POST("tokens")
    public Call<LoginResponse> login(@Body LoginUser loginUser);

    @POST("users")
    public Call<User> signUp(@Body User userCreate);
}
