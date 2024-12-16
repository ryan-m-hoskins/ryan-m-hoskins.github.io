package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public LiveData<UserEntity> getUserByUsernameAndPassword(String username, String password) {
        return userRepository.getUserByUsernameAndPassword(username, password);
    }

    public LiveData<UserEntity> getUserById(int userId) {
        return userRepository.getUserById(userId);
    }
}
