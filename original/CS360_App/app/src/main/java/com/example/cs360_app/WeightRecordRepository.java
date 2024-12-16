package com.example.cs360_app;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WeightRecordRepository {
    private final WeightRecordDAO weightRecordDao;

    public WeightRecordRepository(WeightRecordDAO weightRecordDao) {
        this.weightRecordDao = weightRecordDao;
    }

    public void insertWeightRecord(WeightRecordEntity weightRecordEntity) {
        weightRecordDao.insert(weightRecordEntity);
    }

    public void updateWeightRecord(WeightRecordEntity weightRecordEntity) {
        weightRecordDao.update(weightRecordEntity);
    }

    public void deleteWeightRecord(WeightRecordEntity weightRecordEntity) {
        weightRecordDao.delete(weightRecordEntity);
    }

    public LiveData<List<WeightRecordEntity>> getWeightRecordsLiveData(int userId) {
        return weightRecordDao.getWeightRecordsLiveData(userId);
    }
}
