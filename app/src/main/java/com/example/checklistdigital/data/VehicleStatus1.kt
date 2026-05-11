package com.example.checklistdigital.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicleStatus1")
data class VehicleStatus1 (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val documentos: Boolean,
    val extintor: Boolean,
    val livreto: Boolean,
    val tapetes: Boolean,
    val radio: Boolean,
    val estepe: Boolean,
    val cdPlayer: Boolean,
    val acededorDeCigarro: Boolean,
    val dvdPlayer: Boolean,
    val macaco: Boolean,
    val moduloAmplificador: Boolean,
    val chaveDeRoda: Boolean,
    val frenteCD: Boolean,
    val triangulo: Boolean,
    val antena: Boolean,
    val bateria: Boolean,
    val rodaLigaLeve: Boolean,
    val pintSujaDif: Boolean,
)