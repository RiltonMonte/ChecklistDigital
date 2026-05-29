package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.Client
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ClientViewModel (private val checklistRepository: ChecklistRepository) : ViewModel() {

    var clientUiState by mutableStateOf(ClientUiState())
        private set

    var isEditMode by mutableStateOf(false)
        private set

    fun updateUiState(clientDetails: ClientDetails) {
        clientUiState =
            ClientUiState(clientDetails = clientDetails, isEntryValid = validateInput(clientDetails))
    }

    suspend fun saveClient(): Int {
        return if (validateInput()) {
            if (isEditMode) {
                // Update existing client
                checklistRepository.updateClient(clientUiState.clientDetails.toClient())
                clientUiState.clientDetails.id
            } else {
                // Insert new client
                checklistRepository.insertClient(clientUiState.clientDetails.toClient()).toInt()
            }
        } else {
            -1
        }
    }

    fun loadClientForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val client = checklistRepository.getClient(clientId).first()
                isEditMode = true
                updateUiState(client.toClientDetails())
            } catch (e: Exception) {
                // Handle error
                isEditMode = false
            }
        }
    }

    fun resetForNewClient() {
        isEditMode = false
        clientUiState = ClientUiState()
    }

    private fun validateInput(uiState: ClientDetails = clientUiState.clientDetails): Boolean {
        return with(uiState) {
            serviceDate.isNotBlank() && clientName.isNotBlank() && insurance.isNotBlank() && accident.isNotBlank() && phone.isNotBlank()
        }
    }

    suspend fun getLastClientId(): Int {
        return try {
            val clients = mutableListOf<Client>()
            checklistRepository.getChecklist().collect { clientList ->
                clients.addAll(clientList)
            }
            clients.maxByOrNull { it.id }?.id ?: -1
        } catch (e: Exception) {
            -1
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