package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleStatus2
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VehicleStatus2ViewModel (private val checklistRepository: ChecklistRepository) : ViewModel() {
    var vehicleStatus2UiState by mutableStateOf(VehicleStatus2UiState())
        private set

    var isEditMode by mutableStateOf(false)
        private set

    fun updateUiState(vehicleStatus2Details: VehicleStatus2Details) {
        vehicleStatus2UiState =
            VehicleStatus2UiState(vehicleStatus2Details = vehicleStatus2Details, isEntryValid = validateInput(vehicleStatus2Details))
    }

    suspend fun saveVehicleStatus2(clientId: Int) {
        if (validateInput()) {
            if (isEditMode) {
                // Update existing
                checklistRepository.updateVehicleStatus2(
                    vehicleStatus2UiState.vehicleStatus2Details.toVehicleStatus2(clientId)
                )
            } else {
                // Insert new
                checklistRepository.insertVehicleStatus2(
                    vehicleStatus2UiState.vehicleStatus2Details.toVehicleStatus2(clientId)
                )
            }
        }
    }

    fun loadVehicleStatus2ForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val vehicleStatus2 = checklistRepository.getVehicleStatus2(clientId).first()
                isEditMode = true
                updateUiState(vehicleStatus2.toVehicleStatus2Details())
            } catch (e: Exception) {
                // Handle error
                isEditMode = false
            }
        }
    }

    fun resetForNewVehicleStatus2() {
        isEditMode = false
        vehicleStatus2UiState = VehicleStatus2UiState()
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