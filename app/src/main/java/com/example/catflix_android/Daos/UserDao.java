package com.example.catflix_android.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.catflix_android.Entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> index();

    @Query("SELECT * FROM users WHERE _id=:_id")
    User get(String _id);

    @Insert
    void insert(User...users);

    @Delete
    void delete(User...users);

    @Update
    void update(User...users);
}