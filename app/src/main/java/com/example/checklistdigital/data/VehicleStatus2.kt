package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicleStatus2",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VehicleStatus2 (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientId: Int,
    val pneusDianteiros: Int,
    val pneusTraseiros: Int,
    val estepe: Int,
    val nivelCombustivel: Float,
    val observacoes: String,
)