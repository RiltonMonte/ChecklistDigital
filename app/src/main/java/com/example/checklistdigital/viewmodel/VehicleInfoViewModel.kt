package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleInfo

class VehicleInfoViewModel(private val checklistRepository: ChecklistRepository) : ViewModel() {

    var vehicleInfoUiState by mutableStateOf(vehicleInfoUiState())
        private set

    fun updateUiState(vehicleInfoDetails: VehicleInfoDetails) {
        vehicleInfoUiState =
            vehicleInfoUiState(vehicleInfoDetails = vehicleInfoDetails, isEntryValid = validateInput(vehicleInfoDetails))
    }

    suspend fun saveVehicleInfo(clientId: Int) {
        if (validateInput()) {
            checklistRepository.insertVehicleInfo(
                vehicleInfoUiState.vehicleInfoDetails.toVehicleInfo(clientId)
            )
        }
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