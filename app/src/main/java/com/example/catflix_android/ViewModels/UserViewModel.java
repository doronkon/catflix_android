package com.example.catflix_android.ViewModels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.UserRepository;
public class UserViewModel extends ViewModel {


    private final UserRepository repository;

    public UserViewModel(Context context, LifecycleOwner owner){
        this.repository = new UserRepository(context,owner);
        Log.i("TAG","viewModel Constructor print");
    }
    public void login(MutableLiveData<LoginResponse> loggedUser, LoginUser loginUser) {
        this.repository.login(loggedUser,loginUser);
    }

    public void signUp(MutableLiveData<User> userResponse, User userCreate) {
        this.repository.signUp(userResponse,userCreate);
    }

}
