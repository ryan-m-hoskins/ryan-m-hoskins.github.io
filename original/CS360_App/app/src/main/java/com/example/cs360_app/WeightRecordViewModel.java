package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeightRecordViewModel extends ViewModel {
    private final WeightRecordRepository weightRecordRepository;
    private final ExecutorService executorService;

    public WeightRecordViewModel(WeightRecordRepository weightRecordRepository) {
        this.weightRecordRepository = weightRecordRepository;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // Insertion of weight record
    public void insertWeightRecord(WeightRecordEntity weightRecord) {
        executorService.execute(() -> weightRecordRepository.insertWeightRecord(weightRecord));
    }

    // Update weight record
    public void updateWeightRecord(WeightRecordEntity weightRecord) {
        executorService.execute(() -> weightRecordRepository.updateWeightRecord(weightRecord));
    }

    // Delete weight record
    public void deleteWeightRecord(WeightRecordEntity weightRecord) {
        executorService.execute(() -> weightRecordRepository.deleteWeightRecord(weightRecord));
    }

    // Get all records
    public LiveData<List<WeightRecordEntity>> getWeightRecordsLiveData(int userId) {
        return weightRecordRepository.getWeightRecordsLiveData(userId);
    }
}
