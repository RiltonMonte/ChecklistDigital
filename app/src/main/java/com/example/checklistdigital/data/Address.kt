package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Address",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Address (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientId: Int,
    //endereco origem
    val originStreet: String,
    val originNumber: String,
    val originDistrict: String,
    val originCity: String,
    //endereco destino
    val destinyStreet: String,
    val destinyNumber: String,
    val destinyDistrict: String,
    val destinyCity: String,
)