package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleStatus2

class VehicleStatus2ViewModel (private val checklistRepository: ChecklistRepository) : ViewModel() {
    var vehicleStatus2UiState by mutableStateOf(VehicleStatus2UiState())
        private set

    fun updateUiState(vehicleStatus2Details: VehicleStatus2Details) {
        vehicleStatus2UiState =
            VehicleStatus2UiState(vehicleStatus2Details = vehicleStatus2Details, isEntryValid = validateInput(vehicleStatus2Details))
    }

    suspend fun saveVehicleStatus2(clientId: Int) {
        if (validateInput()) {
            checklistRepository.insertVehicleStatus2(
                vehicleStatus2UiState.vehicleStatus2Details.toVehicleStatus2(clientId)
            )
        }
    }

    private fun validateInput(uiState: VehicleStatus2Details = vehicleStatus2UiState.vehicleStatus2Details): Boolean {
        return with(uiState) {
            observacoes.isNotBlank()
        }
    }
}

data class VehicleStatus2Details (
    val id: Int = 0,
    val pneusDianteiros: Int = 0,
    val pneusTraseiros: Int = 0,
    val estepe: Int = 0,
    val nivelCombustivel: Float = .0f,
    val observacoes: String = "",
)

data class VehicleStatus2UiState(
    val vehicleStatus2Details: VehicleStatus2Details = VehicleStatus2Details(),
    val isEntryValid: Boolean = false
)

fun VehicleStatus2Details.toVehicleStatus2(clientId: Int): VehicleStatus2 = VehicleStatus2(
    id = id,
    clientId = clientId,
    pneusDianteiros = pneusDianteiros,
    pneusTraseiros = pneusTraseiros,
    estepe = estepe,
    nivelCombustivel = nivelCombustivel,
    observacoes = observacoes
)

fun VehicleStatus2.toVehicleStatus2Details(): VehicleStatus2Details = VehicleStatus2Details(
    id = id,
    pneusDianteiros = pneusDianteiros,
    pneusTraseiros = pneusTraseiros,
    estepe = estepe,
    nivelCombustivel = nivelCombustivel,
    observacoes = observacoes
)

fun VehicleStatus2.toVehicleStatus2UiState(isEntryValid: Boolean = false): VehicleStatus2UiState = VehicleStatus2UiState(
    vehicleStatus2Details = this.toVehicleStatus2Details(),
    isEntryValid = isEntryValid
)