package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VehicleInfoViewModel(private val checklistRepository: ChecklistRepository) : ViewModel() {

    var vehicleInfoUiState by mutableStateOf(vehicleInfoUiState())
        private set

    var isEditMode by mutableStateOf(false)
        private set

    fun updateUiState(vehicleInfoDetails: VehicleInfoDetails) {
        vehicleInfoUiState =
            vehicleInfoUiState(vehicleInfoDetails = vehicleInfoDetails, isEntryValid = validateInput(vehicleInfoDetails))
    }

    suspend fun saveVehicleInfo(clientId: Int) {
        if (validateInput()) {
            if (isEditMode) {
                // Update existing vehicle info
                checklistRepository.updateVehicleInfo(
                    vehicleInfoUiState.vehicleInfoDetails.toVehicleInfo(clientId)
                )
            } else {
                // Insert new vehicle info
                checklistRepository.insertVehicleInfo(
                    vehicleInfoUiState.vehicleInfoDetails.toVehicleInfo(clientId)
                )
            }
        }
    }

    fun loadVehicleInfoForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val vehicleInfo = checklistRepository.getVehicleInfoByClientId(clientId).first()
                isEditMode = true
                updateUiState(vehicleInfo.toVehicleInfoDetails())
            } catch (e: Exception) {
                // Handle error - vehicle info might not exist
                isEditMode = false
            }
        }
    }

    fun resetForNewVehicle() {
        isEditMode = false
        vehicleInfoUiState = vehicleInfoUiState()
    }

    private fun validateInput(uiState: VehicleInfoDetails = vehicleInfoUiState.vehicleInfoDetails): Boolean {
        return with(uiState) {
            vehicle.isNotBlank() && plate.isNotBlank() && color.isNotBlank() && year.isNotBlank()
        }
    }


}

data class VehicleInfoDetails(
    val id: Int = 0,
    val vehicle: String = "",
    val plate: String = "",
    val color: String = "",
    val year: String = "",
)

data class vehicleInfoUiState(
    val vehicleInfoDetails: VehicleInfoDetails = VehicleInfoDetails(),
    val isEntryValid: Boolean = false
)

fun VehicleInfoDetails.toVehicleInfo(clientId: Int): VehicleInfo = VehicleInfo(
    id = id,
    clientId = clientId,
    vehicle = vehicle,
    plate = plate,
    color = color,
    year = year,
)

fun VehicleInfo.toVehicleInfoDetails(): VehicleInfoDetails = VehicleInfoDetails(
    id = id,
    vehicle = vehicle,
    plate = plate,
    color = color,
    year = year,
)

fun VehicleInfo.toVehicleInfoUiState(isEntryValid: Boolean = false): vehicleInfoUiState = vehicleInfoUiState(
    vehicleInfoDetails = this.toVehicleInfoDetails(),
    isEntryValid = isEntryValid
)