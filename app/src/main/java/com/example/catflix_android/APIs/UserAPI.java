package com.example.catflix_android.APIs;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.DataManager;
import com.example.catflix_android.Entities.LoginResponse;
import com.example.catflix_android.Entities.LoginUser;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.WebServices.UserWebService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    Retrofit retrofit;
    UserWebService webService;

    public UserAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/") // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webService = retrofit.create(UserWebService.class);
    }

    public void login(MutableLiveData<LoginResponse> loggedUser, String name, String password) {
        LoginUser loginUser = new LoginUser(name, password);
        Call<LoginResponse> call = webService.login(loginUser);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loggedUser2 = response.body();
                    // Update DataManager with the response details
                    DataManager instance = DataManager.getInstance();
                    DataManager.setCurrentUserID(loggedUser2.getId());
                    DataManager.setIsAdmin(loggedUser2.isAdmin());
                    DataManager.setToken(loggedUser2.getToken());
                    loggedUser.setValue(loggedUser2);
                    System.out.println("returning ID: " + loggedUser2.getId());


                } else {

                    DataManager instance = DataManager.getInstance();
                    DataManager.setCurrentUserID(null);
                    DataManager.setIsAdmin(null);
                    DataManager.setToken(null);
                    loggedUser.setValue(null);
                    // Handle HTTP error responses
                    // Example: Log error or show a toast
                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle network failure
                // Example: Log the error or show a toast
                DataManager instance = DataManager.getInstance();
                DataManager.setCurrentUserID(null);
                DataManager.setIsAdmin(null);
                DataManager.setToken(null);
                loggedUser.setValue(null);

                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }
}
