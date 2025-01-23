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
    private final Context context;


    private MutableLiveData<User> user;

    private UserRepository repository;

    public UserViewModel(Context context, LifecycleOwner owner){
        this.context = context;
        this.repository = new UserRepository(context,owner);
        Log.i("TAG","viewModel Constructor print");
    }
    public MutableLiveData<User> getUserData(){
        if(user==null){
            return new MutableLiveData<>();
        }
        return user;
    }
    public void getUser(MutableLiveData<User> userResponse) {
        repository.getUser(userResponse);
    }
    public void login(MutableLiveData<LoginResponse> loggedUser, LoginUser loginUser) {
        Log.i("TAG","viewModel Login print");

        this.repository.login(loggedUser,loginUser);
    }

    public void signUp(MutableLiveData<User> userResponse, User userCreate) {
        Log.i("TAG","viewModel signup print");

        this.repository.signUp(userResponse,userCreate);
    }

}
