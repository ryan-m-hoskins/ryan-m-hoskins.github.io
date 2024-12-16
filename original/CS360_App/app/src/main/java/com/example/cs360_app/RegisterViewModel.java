package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final ExecutorService executorService;

    public RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<UserEntity> findUserByUsernameOrEmail(String username, String email) {
        return userRepository.findUserByUsernameOrEmail(username, email);
    }

    public void insertUser(UserEntity user) {
        executorService.execute(() -> userRepository.insertUser(user));
    }
}
