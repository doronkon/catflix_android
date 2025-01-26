package com.example.catflix_android.APIs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.DataTypes.StringMovie;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.WebServices.MovieWebService;
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

public class MovieAPI {
    Retrofit retrofit;
    MovieWebService webService;

    public MovieAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/") // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webService = retrofit.create(MovieWebService.class);
    }
    public void getMovies(MutableLiveData<MoviesResponse> moviesResponse,Context context) {
        String user = DataManager.getTokenHeader();
        Call<MoviesResponse> call = webService.getMovies(user);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoviesResponse returnedMovies = response.body();
                    moviesResponse.setValue(returnedMovies);

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
                    moviesResponse.setValue(null);

                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                moviesResponse.setValue(null);

                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }
    public void index(MutableLiveData<List<Movie>> moviesResponse, Context context) {
        Call<List<Movie>> call = webService.index();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> returnedMovies = response.body();
                    moviesResponse.setValue(returnedMovies);

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
                    moviesResponse.setValue(null);

                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                moviesResponse.setValue(null);
                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }


    public void patchMovieForUser(String movie, Context context) {
        StringMovie baruchHashem = new StringMovie(movie);

        Thread firstPatch = new Thread(()->patchInMongo(baruchHashem, context));
        firstPatch.start();
        try{
            firstPatch.join();
            //patchInCpp(movie,context);
        }catch (Exception ex)
        {
            Log.w("THREAD ERROR", ex);
            Thread.currentThread().interrupt();
        }
    }


    public void patchInMongo(StringMovie baruchHashem, Context context){
        webService.addToNode(DataManager.getCurrentUserId(), DataManager.getTokenHeader(), baruchHashem)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            handleError(response, context);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        handleFailure(t, context);
                    }
                });
    }

    public void patchInCpp(String movie, Context context){
        webService.addToCPP(DataManager.getTokenHeader(), movie)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            handleError(response, context);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        handleFailure(t, context);
                    }
                });
    }

    public void getCppRecommendation(String movieId, Context context, MutableLiveData <List<Movie>> currentRecommendation) {
        webService.getCppRecommendation(DataManager.getTokenHeader(), movieId)
                .enqueue(new Callback<List<Movie>>() {
                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> returnedMovies = response.body();
                            currentRecommendation.setValue(returnedMovies);
                        } else {
                            handleError(response, context);
                            currentRecommendation.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        handleFailure(t, context);
                        currentRecommendation.setValue(null);
                    }
                });
    }




    // Method to handle error response from Retrofit
    private void handleError(Response<?> response, Context context) {
        try {
            String errorMessage = response.errorBody().string();
            JSONObject errorObject = new JSONObject(errorMessage);
            JSONArray errorsArray = errorObject.getJSONArray("errors");
            errorMessage = errorsArray.getString(0);

            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
        }
    }

    // Method to handle failure
    private void handleFailure(Throwable t, Context context) {
        t.printStackTrace();
        Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }


}
