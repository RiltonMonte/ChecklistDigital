package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleStatus1
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VehicleStatus1ViewModel ( private val checklistRepository: ChecklistRepository) : ViewModel() {
    var vehicleStatus1UiState by mutableStateOf(VehicleStatus1UiState())
        private set

    var isEditMode by mutableStateOf(false)
        private set

    fun updateUiState(vehicleStatus1Details: VehicleStatus1Details) {
        vehicleStatus1UiState =
            VehicleStatus1UiState(vehicleStatus1Details = vehicleStatus1Details)
    }

    suspend fun saveVehicleStatus1(clientId: Int) {
        if (isEditMode) {
            // Update existing
            checklistRepository.updateVehicleStatus1(
                vehicleStatus1UiState.vehicleStatus1Details.toVehicleStatus1(clientId)
            )
        } else {
            // Insert new
            checklistRepository.insertVehicleStatus1(
                vehicleStatus1UiState.vehicleStatus1Details.toVehicleStatus1(clientId)
            )
        }
    }

    fun loadVehicleStatus1ForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val vehicleStatus1 = checklistRepository.getVehicleStatus1(clientId).first()
                isEditMode = true
                updateUiState(vehicleStatus1.toVehicleStatus1Details())
            } catch (e: Exception) {
                // Handle error
                isEditMode = false
            }
        }
    }

    fun resetForNewVehicleStatus1() {
        isEditMode = false
        vehicleStatus1UiState = VehicleStatus1UiState()
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

fun VehicleStatus1Details.toVehicleStatus1(clientId: Int): VehicleStatus1 = VehicleStatus1(
    id = id,
    clientId = clientId,
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