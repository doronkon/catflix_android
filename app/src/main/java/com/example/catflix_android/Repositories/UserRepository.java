package com.example.catflix_android.Repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.APIs.UserAPI;
import com.example.catflix_android.AppDB;
import com.example.catflix_android.Daos.UserDao;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.Entities.User;

public class UserRepository {
    private UserDao dao;
    private UserData userData;
    private UserAPI api;
    private Context context;

    private LifecycleOwner owner;

    public UserRepository(Context context, LifecycleOwner owner) {
        this.context = context;
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

    public void login(MutableLiveData<LoginResponse> loggedUser, LoginUser loginUser) {
        this.api.login(loggedUser,loginUser,context);
    }

    public void signUp(MutableLiveData<User> userResponse, User userCreate) {
        this.api.signUp(userResponse, userCreate, context);
    }
    public void getUser(MutableLiveData<User> userResponse) {
        this.api.getUser(userResponse, context);
    }
}
