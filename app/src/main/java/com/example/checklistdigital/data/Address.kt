package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Address")
data class Address (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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