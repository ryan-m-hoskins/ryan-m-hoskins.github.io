package com.example.cs499_app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//
@Entity(
    tableName = "weight_records",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WeightRecordEntity (
    @field:PrimaryKey(autoGenerate = true)
    val id: Int,

    @field:ColumnInfo(name = "weight")
    var weight: String,

    @field:ColumnInfo(name = "date")
    var date: String,

    @field:ColumnInfo(name = "user_id")
    var userId: Int
)


// TODO: Delete file and test app