package com.example.cs499_app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "UserEntity")
class UserEntity // Constructor
    (
    @field:ColumnInfo(name = "username") var username: String,
    @field:ColumnInfo(name = "password") var password: String,
    @field:ColumnInfo(
        name = "email"
    ) var email: String,
    @field:ColumnInfo(name = "phone") var phone: String
) {
    // == Getters and Setters == //
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

// TODO: Delete file and test app