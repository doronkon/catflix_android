package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.UserRepository;

public class CurrentUserViewModel extends ViewModel {

    LiveData<User> currentUser;
    private final UserRepository repository;

    public CurrentUserViewModel(Context context, LifecycleOwner owner){
        this.repository = new UserRepository(context,owner);
        currentUser = this.repository.getCurrentUser();

    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
}
