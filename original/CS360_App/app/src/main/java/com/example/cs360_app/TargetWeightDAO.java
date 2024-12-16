package com.example.cs360_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TargetWeightDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TargetWeightEntity targetWeightEntity);

    @Update
    void update(TargetWeightEntity targetWeightEntity);

    @Delete
    void delete(TargetWeightEntity targetWeightEntity);


    @Query("SELECT * FROM TargetWeightEntity WHERE user_id  = :userId")
    LiveData<TargetWeightEntity> getTargetWeight(int userId);
}
