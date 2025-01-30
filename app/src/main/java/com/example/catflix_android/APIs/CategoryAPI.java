package com.example.catflix_android.APIs;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.MoviesResponse;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.WebServices.CategoryWebService;
import com.example.catflix_android.WebServices.MovieWebService;
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

public class CategoryAPI {
    Retrofit retrofit;
    CategoryWebService webService;


    public CategoryAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/") // Replace with your actual API base URL
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webService = retrofit.create(CategoryWebService.class);
    }
    public void fetchCategories(MutableLiveData<List<Category>> categoriesResponse, Context context) {
        String user = DataManager.getTokenHeader();
        Call<List<Category>> call = webService.getCategories(user);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> returnedCategories = response.body();
                    categoriesResponse.setValue(returnedCategories);

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
                    categoriesResponse.setValue(null);

                    System.err.println("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                categoriesResponse.setValue(null);

                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }
    public void deleteCategory(String categoryId, Context context, MutableLiveData<Boolean> flag) {
        String user = DataManager.getTokenHeader();

        Call<Void> call = webService.deleteCategory(user, categoryId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    flag.setValue(true);
                } else {
                    handleError(response, context);
                    flag.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }


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

    public void editCategory(String categoryId, String newCatName, boolean flag, Context context){
        String user = DataManager.getTokenHeader();
        Category pojo = new Category(categoryId,newCatName,flag);
        Call<Void> call = webService.editCategory(user, categoryId, pojo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Category edited successfully!", Toast.LENGTH_SHORT).show();

                } else {
                    handleError(response, context);
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }

    public void createCategory(String newCatName, boolean flag, Context context){
        String user = DataManager.getTokenHeader();
        Category pojo = new Category(null,newCatName,flag);
        Call<Void> call = webService.createCategory(user, pojo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Category edited successfully!", Toast.LENGTH_SHORT).show();

                } else {
                    handleError(response, context);
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }
    public void fetchCategoryMovies(MutableLiveData<List<Movie>> categoryMovies,String CategoryID){

        String user = DataManager.getTokenHeader();
        Call<List<Movie>> call = webService.fetchCategoryMovies(user, CategoryID);

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> returnedCategoryMovies = response.body();
                    categoryMovies.setValue(returnedCategoryMovies);

                } else {
                    categoryMovies.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                categoryMovies.setValue(null);
                System.err.println("Network Error: " + t.getMessage());
            }
        });
    }
}
