package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleStatus1

class VehicleStatus1ViewModel ( private val checklistRepository: ChecklistRepository) : ViewModel() {
    var vehicleStatus1UiState by mutableStateOf(VehicleStatus1UiState())
        private set

    fun updateUiState(vehicleStatus1Details: VehicleStatus1Details) {
        vehicleStatus1UiState =
            VehicleStatus1UiState(vehicleStatus1Details = vehicleStatus1Details)
    }

    suspend fun saveVehicleStatus1() {
            checklistRepository.insertVehicleStatus1(vehicleStatus1UiState.vehicleStatus1Details.toVehicleStatus1())
    }


}

data class VehicleStatus1Details (
    val id: Int = 0,
    val documentos: Boolean = false,
    val extintor: Boolean = false,
    val livreto: Boolean = false,
    val tapetes: Boolean = false,
    val radio: Boolean = false,
    val estepe: Boolean = false,
    val cdPlayer: Boolean = false,
    val acededorDeCigarro: Boolean = false,
    val dvdPlayer: Boolean = false,
    val macaco: Boolean = false,
    val moduloAmplificador: Boolean = false,
    val chaveDeRoda: Boolean = false,
    val frenteCD: Boolean = false,
    val triangulo: Boolean = false,
    val antena: Boolean = false,
    val bateria: Boolean = false,
    val rodaLigaLeve: Boolean = false,
    val pintSujaDif: Boolean = false,
)

data class VehicleStatus1UiState(
    val vehicleStatus1Details: VehicleStatus1Details = VehicleStatus1Details(),
    val isEntryValid: Boolean = true
)

fun VehicleStatus1Details.toVehicleStatus1(): VehicleStatus1 = VehicleStatus1(
    id = id,
    documentos = documentos,
    extintor = extintor,
    livreto = livreto,
    tapetes = tapetes,
    radio = radio,
    estepe = estepe,
    cdPlayer = cdPlayer,
    acededorDeCigarro = acededorDeCigarro,
    dvdPlayer = dvdPlayer,
    macaco = macaco,
    moduloAmplificador = moduloAmplificador,
    chaveDeRoda = chaveDeRoda,
    frenteCD = frenteCD,
    triangulo = triangulo,
    antena = antena,
    bateria = bateria,
    rodaLigaLeve = rodaLigaLeve,
    pintSujaDif = pintSujaDif,
)

fun VehicleStatus1.toVehicleStatus1Details(): VehicleStatus1Details = VehicleStatus1Details(
    id = id,
    documentos = documentos,
    extintor = extintor,
    livreto = livreto,
    tapetes = tapetes,
    radio = radio,
    estepe = estepe,
    cdPlayer = cdPlayer,
    acededorDeCigarro = acededorDeCigarro,
    dvdPlayer = dvdPlayer,
    macaco = macaco,
    moduloAmplificador = moduloAmplificador,
    chaveDeRoda = chaveDeRoda,
    frenteCD = frenteCD,
    triangulo = triangulo,
    antena = antena,
    bateria = bateria,
    rodaLigaLeve = rodaLigaLeve,
    pintSujaDif = pintSujaDif,
)

fun VehicleStatus1.toVehicleStatus1UiState(isEntryValid: Boolean = false): VehicleStatus1UiState = VehicleStatus1UiState(
    vehicleStatus1Details = this.toVehicleStatus1Details(),
    isEntryValid = isEntryValid
)