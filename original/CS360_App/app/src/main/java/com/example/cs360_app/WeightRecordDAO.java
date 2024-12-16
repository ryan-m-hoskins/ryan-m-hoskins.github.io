package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeightRecordDAO {
    @Insert
    void insert(WeightRecordEntity weightRecordEntity);

    @Update
    void update(WeightRecordEntity weightRecordEntity);

    @Delete
    void delete(WeightRecordEntity weightRecordEntity);

    @Query("SELECT * FROM weight_records WHERE user_id = :userId")
    LiveData<List<WeightRecordEntity>> getWeightRecordsLiveData(int userId);

}
