package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TargetWeightViewModel extends ViewModel {
    private final TargetWeightRepository targetWeightRepository;

    // Constructor
    public TargetWeightViewModel(TargetWeightRepository targetWeightRepository) {
        this.targetWeightRepository = targetWeightRepository;
    }

    // Insert target weight
    public void insertTargetWeight(TargetWeightEntity targetWeightEntity) {
        targetWeightRepository.insertTargetWeight(targetWeightEntity);
    }

    // Update target weight
    public void updateTargetWeight(TargetWeightEntity targetWeightEntity) {
        targetWeightRepository.updateTargetWeight(targetWeightEntity);
    }
    // Get target weight
    public LiveData<TargetWeightEntity> getTargetWeight(int userId) {
        return targetWeightRepository.getTargetWeight(userId);
    }
}
