package com.example.catflix_android.ViewModels;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.LoginResponse;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.UserRepository;
public class UserViewModel extends ViewModel {
    private final Context context;

    private String name;
    private String password;

    private MutableLiveData<User> user;

    private UserRepository repository;

    public UserViewModel(Context context, LifecycleOwner owner,String name,String password){
        this.name = name;
        this.password = password;
        this.context = context;
        this.repository = new UserRepository(context,owner,name,password);
        Log.i("TAG","viewModel Constructor print");
    }
    public MutableLiveData<User> getUserData(){
        if(user==null){
            return new MutableLiveData<>();
        }
        return user;
    }
    public void getUser(String username) {
        repository.getUser(username);
    }
    public void login(MutableLiveData<LoginResponse> loggedUser) {
        Log.i("TAG","viewModel Login print");

        this.repository.login(loggedUser);
    }



}
