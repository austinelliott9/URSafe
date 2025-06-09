package com.example.ursafe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class Credential(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceName: String,
    val username: String,
    val password: String
)