package com.example.catflix_android.APIs;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.WebServices.UserWebService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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

    public void login(MutableLiveData<LoginResponse> loggedUser, LoginUser loginUser,Context context) {
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
                    // Show a Toast message with the error response
                    try {
                        // Get the error message from the response body
                        String errorMessage = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorMessage);
                        JSONArray errorsArray = errorObject.getJSONArray("errors");
                        errorMessage = errorsArray.getString(0);

                        // Show a Toast with the error message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        // Handle error if errorBody cannot be converted to string
                        e.printStackTrace();
                        Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    // Handle HTTP error responses
                    // Example: Log error or show a toast
                    loggedUser.setValue(null);

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


    public void signUp(MutableLiveData<User> userResponse, User userCreate,Context context) {
        Call<User> call = webService.signUp(userCreate);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User createdUser = response.body();
                    userResponse.setValue(createdUser);

                } else {
                    try {
                        // Get the error message from the response body
                        String errorMessage = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorMessage);
                        JSONArray errorsArray = errorObject.getJSONArray("errors");
                        errorMessage = errorsArray.getString(0);

                        // Show a Toast with the error message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        // Handle error if errorBody cannot be converted to string
                        e.printStackTrace();
                        Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    // Handle HTTP error responses
                    // Example: Log error or show a toast
                    userResponse.setValue(null);

                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userResponse.setValue(null);

                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }
    public void getCurrentUser(MutableLiveData<User> userResponse,Context context) {
        String id = DataManager.getCurrentUserId();
        String token = DataManager.getTokenHeader();
        Call<User> call = webService.getUser(token,id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User exisitingUser = response.body();
                    userResponse.setValue(exisitingUser);

                } else {
                    try {
                        // Get the error message from the response body
                        String errorMessage = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorMessage);
                        JSONArray errorsArray = errorObject.getJSONArray("errors");
                        errorMessage = errorsArray.getString(0);

                        // Show a Toast with the error message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        // Handle error if errorBody cannot be converted to string
                        e.printStackTrace();
                        Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    // Handle HTTP error responses
                    // Example: Log error or show a toast
                    userResponse.setValue(null);

                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userResponse.setValue(null);

                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }

    public void index(MutableLiveData<List<User>> usersResponse, Context context) {
        Call<List<User>> call = webService.index();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> returnedUsers = response.body();
                    usersResponse.setValue(returnedUsers);

                } else {
                    try {
                        // Get the error message from the response body
                        String errorMessage = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorMessage);
                        JSONArray errorsArray = errorObject.getJSONArray("errors");
                        errorMessage = errorsArray.getString(0);

                        // Show a Toast with the error message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        // Handle error if errorBody cannot be converted to string
                        e.printStackTrace();
                        Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }
                    // Handle HTTP error responses
                    // Example: Log error or show a toast
                    usersResponse.setValue(null);

                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                usersResponse.setValue(null);
                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }



}
