package com.example.cs360_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = {"user_id"}, unique = true)
)
public class TargetWeightEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "target_weight")
    private String targetWeight;

    @ColumnInfo(name = "user_id")
    private int userId;

    // == Getters and Setters == //
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }
}
