package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void Insert(UserEntity user);

    @Update
    void Update(UserEntity user);

    @Delete
    void Delete(UserEntity user);

    @Query("SELECT * FROM UserEntity")
    List<UserEntity> getAll();

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    LiveData<UserEntity> getUserById(int id);

    @Query("SELECT * FROM UserEntity WHERE username = :username OR email = :email LIMIT 1")
    LiveData<UserEntity> findUserByUsernameOrEmailLiveData(String username, String email);

    @Query("SELECT * FROM UserEntity WHERE username = :username AND password = :password LIMIT 1")
    LiveData<UserEntity> findUserByUsernameAndPasswordLiveData(String username, String password);
}
