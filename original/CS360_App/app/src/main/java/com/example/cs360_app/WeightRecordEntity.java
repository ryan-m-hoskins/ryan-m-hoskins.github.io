package com.example.cs360_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
//
@Entity(
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        ),
        tableName = "weight_records"
)

public class WeightRecordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "weight")
    private String weight;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "user_id")
    private int userId;

    // Constructor
    public WeightRecordEntity(int id, String weight, String date, int userId) {
        this.id = id;
        this.weight = weight;
        this.date = date;
        this.userId = userId;
    }

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
