package com.example.catflix_android.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.catflix_android.Entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    List<Category> index();

    @Query("SELECT * FROM categories WHERE _id=:id")
    Category get(String id);

    @Insert
    void insert(Category...categories);

    @Delete
    void delete(Category...categories);

    @Update
    void update(Category...categories);
}