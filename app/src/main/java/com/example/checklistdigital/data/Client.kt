package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "client")
data class Client (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceDate: String,
    val clientName: String,
    val insurance: String,
    val accident: String,
    val phone: String,
)