package com.example.catflix_android.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Repositories.UserRepository;

public class CurrentUserViewModel extends ViewModel {

    LiveData<User> currentUser;
    MutableLiveData<Boolean> editEnded;
    private final UserRepository repository;

    public CurrentUserViewModel(Context context, LifecycleOwner owner){
        this.repository = new UserRepository(context,owner);
        currentUser = this.repository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    public LiveData<Boolean> getEditEnded() {
        if(editEnded == null)
        {
            editEnded = new MutableLiveData<>();
        }
        return editEnded;
    }
    public void editUser(User userEdit)
    {
        this.repository.editUser(userEdit,this.editEnded);
    }
}
