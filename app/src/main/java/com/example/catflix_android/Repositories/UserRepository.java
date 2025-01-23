package com.example.catflix_android.Repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.UserDao;
import com.example.catflix_android.Entities.LoginResponse;
import com.example.catflix_android.Entities.User;

public class UserRepository {
    private String name;
    private String password;
    private UserDao dao;
    private UserData userData;
    private UserAPI api;
    private Context context;

    private LifecycleOwner owner;

    public UserRepository(Context context, LifecycleOwner owner, String name,String password) {
        this.name = name;
        this.context = context;
        this.password = password;
        api = new UserAPI();
        userData = new UserData();
        AppDB database = AppDB.getInstance(context);
        dao = database.userDao();
        this.owner = owner;
    }

    class UserData extends MutableLiveData<User> {
        public UserData(String username) {
            super();
            getUser(username);
        }

        public UserData() {
            super();
        }

        //        @Override
        protected void onActive() {
            super.onActive();
        }
    }

    public void getUser(String username) {
        new Thread(() -> {
            User user = dao.get(username);
            userData.postValue(user);
        }).start();
    }

    public MutableLiveData<User> get() {
        return userData;
    }

    public void login(MutableLiveData<LoginResponse> loggedUser) {
        this.api.login(loggedUser,name,password,context);
    }

    public void signUp(MutableLiveData<User> userResponse, User userCreate) {
        this.api.signUp(userResponse, userCreate, context);
    }
}
