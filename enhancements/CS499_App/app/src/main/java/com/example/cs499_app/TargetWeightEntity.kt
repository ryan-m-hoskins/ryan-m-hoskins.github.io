package com.example.cs499_app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["user_id"], unique = true)]
)
class TargetWeightEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "target_weight")
    var targetWeight: String? = null

    @ColumnInfo(name = "user_id")
    var userId: Int = 0
}

// TODO: Delete file and test app