package com.example.cs360_app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {WeightRecordEntity.class, UserEntity.class, TargetWeightEntity.class}, version = 4)
public abstract class WeightTrackingDatabase extends RoomDatabase {
    public abstract WeightRecordDAO weightRecordDAO();
    public abstract UserDAO userDAO();
    public abstract TargetWeightDAO targetWeightDAO();

    private static volatile WeightTrackingDatabase INSTANCE;

    public static WeightTrackingDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WeightTrackingDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WeightTrackingDatabase.class,
                                    "weight_tracking_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}