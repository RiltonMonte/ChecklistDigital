package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicleStatus2")
data class VehicleStatus2 (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pneusDianteiros: Int,
    val pneusTraseiros: Int,
    val estepe: Int,
    val nivelCombustivel: Float,
    val observacoes: String,
)