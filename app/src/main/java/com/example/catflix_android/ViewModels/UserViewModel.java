package com.example.catflix_android.ViewModels;


import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.UserRepository;
public class UserViewModel extends ViewModel {
    private String name;
    private String password;

    private MutableLiveData<User> user;

    private UserRepository repository;

    public UserViewModel(Context context, LifecycleOwner owner,String name,String password){
        this.name = name;
        this.password = password;
        this.repository = new UserRepository(context,owner,name,password);
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
    public void login() {
        this.repository.login();
    }



}
