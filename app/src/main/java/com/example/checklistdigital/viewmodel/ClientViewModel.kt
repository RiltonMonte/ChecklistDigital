package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.Client

class ClientViewModel (private val checklistRepository: ChecklistRepository) : ViewModel() {

    var clientUiState by mutableStateOf(ClientUiState())
        private set

    fun updateUiState(clientDetails: ClientDetails) {
        clientUiState =
            ClientUiState(clientDetails = clientDetails, isEntryValid = validateInput(clientDetails))
    }

    suspend fun saveClient() {
        if (validateInput()) {
            checklistRepository.insertClient(clientUiState.clientDetails.toClient())
        }
    }

    private fun validateInput(uiState: ClientDetails = clientUiState.clientDetails): Boolean {
        return with(uiState) {
            serviceDate.isNotBlank() && clientName.isNotBlank() && insurance.isNotBlank() && accident.isNotBlank() && phone.isNotBlank()
        }
    }
}

data class ClientDetails(
    val id: Int = 0,
    val serviceDate: String = "",
    val clientName: String = "",
    val insurance: String = "",
    val accident: String = "",
    val phone: String = "",
)

data class ClientUiState(
    val clientDetails: ClientDetails = ClientDetails(),
    val isEntryValid: Boolean = false
)

fun ClientDetails.toClient(): Client = Client(
    id = id,
    serviceDate = serviceDate,
    clientName = clientName,
    insurance = insurance,
    accident = accident,
    phone = phone,
)

fun Client.toClientDetails(): ClientDetails = ClientDetails(
    id = id,
    serviceDate = serviceDate,
    clientName = clientName,
    insurance = insurance,
    accident = accident,
    phone = phone,
)

fun Client.toClientUiState(isEntryValid: Boolean = false): ClientUiState = ClientUiState(
    clientDetails = this.toClientDetails(),
    isEntryValid = isEntryValid
)