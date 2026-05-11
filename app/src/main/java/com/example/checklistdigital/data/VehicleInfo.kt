package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicleInfo")
data class VehicleInfo (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vehicle: String,
    val plate: String,
    val color: String,
    val year: String,
)

