package com.example.cs360_app;

import androidx.lifecycle.LiveData;

public class TargetWeightRepository {
    private final TargetWeightDAO targetWeightDAO;

    public TargetWeightRepository(TargetWeightDAO targetWeightDAO){
        this.targetWeightDAO = targetWeightDAO;
    }

    // Insert target weight
    public void insertTargetWeight(TargetWeightEntity targetWeightEntity){
        new Thread(()-> targetWeightDAO.insert(targetWeightEntity)).start();
    }

    // Update target weight
    public void updateTargetWeight(TargetWeightEntity targetWeightEntity) {
        new Thread(()-> targetWeightDAO.update(targetWeightEntity)).start();
    }

    // Get target weight based on userId
    public LiveData<TargetWeightEntity> getTargetWeight(int userId) {
        return targetWeightDAO.getTargetWeight(userId);
    }
}
