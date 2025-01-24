package com.example.catflix_android.Repositories;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.Activities.ProfileActivity;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.UserDao;
import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;

public class UserRepository {
    private final UserDao dao;
    private MutableLiveData<User> currentUser;
    private final UserAPI api;
    private final Context context;

    private LifecycleOwner owner;

    public UserRepository(Context context, LifecycleOwner owner) {
        this.context = context;
        api = new UserAPI();
        AppDB database = AppDB.getInstance(context);
        dao = database.userDao();
        this.owner = owner;
    }


    public void login(MutableLiveData<LoginResponse> loggedUser, LoginUser loginUser) {
        this.api.login(loggedUser,loginUser,context);
    }

    public void signUp(MutableLiveData<User> userResponse, User userCreate) {
        this.api.signUp(userResponse, userCreate, context);
    }
    public LiveData<User> getCurrentUser() {
        //we check is there a user
        if(this.currentUser != null)
        {
            return this.currentUser;
        }
        currentUser = new MutableLiveData<>();
        //gets the user and posts it in MTLD- mutable live data
        Thread getUserThread = new Thread(() -> this.currentUser.postValue(this.dao.get(DataManager.getCurrentUserId())));
// Wait for the delete thread to finish without blocking the main thread.
        getUserThread.start();
        try {
            getUserThread.join();
        } catch (Exception ex) {
            Log.w("THREAD ERROR", ex);
            Thread.currentThread().interrupt();
        }
        //finished because of the join
        return this.currentUser;
    }
}
