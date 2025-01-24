package com.example.catflix_android.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(
        tableName = "categories"
)
public class Category {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String _id;
    private String name;

    private boolean promoted;

    public Category(String _id, String name, boolean promoted) {
        this._id = _id;
        this.name = name;
        this.promoted = promoted;
    }
    public String get_id(){
        return this._id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public String getName() {
        return name;
    }


    public boolean isPromoted() {
        return promoted;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", promoted=" + promoted +
                '}';
    }
}
