package com.example.catflix_android.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Entity(
        tableName = "users"
)
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String _id;
    private boolean admin;
    private String name;
    private String displayName;
    private String password;
    private String email;
    private String image;
    //private ArrayList<Movie> moviesWatched;

    public User(String _id, String image, String email, String password, String displayName, String name, boolean admin) {
        this._id = _id;
        ///this.moviesWatched = moviesWatched;
        this.image = image;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.name = name;
        this.admin = admin;
    }
    public String get_id(){
        return this._id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

//    public ArrayList<Movie> getMoviesWatched() {
//        return moviesWatched;
//    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public void setMoviesWatched(Movie movie) {
//        if(!this.moviesWatched.contains(movie)){
//            this.moviesWatched.add(movie);
//        }
//    }

    @Override
    public String toString() {
        return "User{" +
                "admin=" + admin +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
//                ", moviesWatched=" + moviesWatched +
                '}';
    }
}
