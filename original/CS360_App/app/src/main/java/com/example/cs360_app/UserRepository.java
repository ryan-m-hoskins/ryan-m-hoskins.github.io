package com.example.cs360_app;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private final UserDAO userDao;

    public UserRepository(UserDAO userDao) {
        this.userDao = userDao;
    }

    public LiveData<UserEntity> getUserByUsernameAndPassword(String username, String password) {
        return userDao.findUserByUsernameAndPasswordLiveData(username, password);
    }
    public LiveData<UserEntity> getUserById(int userId) {
        return userDao.getUserById(userId);
    }
    public LiveData<UserEntity> findUserByUsernameOrEmail(String username, String email) {
        return userDao.findUserByUsernameOrEmailLiveData(username, email);
    }
    public void insertUser(UserEntity user) {
        userDao.Insert(user);
    }
}
