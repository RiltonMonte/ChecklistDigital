package com.example.checklistdigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ChecklistSummary(
    val clientId: Int,
    val insurance: String,
    val serviceDate: String,
    val vehicle: String,
    val plate: String,
    val phone: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChecklistHomeUiState(
    val checklists: List<ChecklistSummary> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedClientId: Int? = null  // Track selected checklist
)

class ChecklistHomeViewModel(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChecklistHomeUiState())
    val uiState: StateFlow<ChecklistHomeUiState> = _uiState.asStateFlow()

    init {
        loadChecklists()
    }

    fun loadChecklists() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                checklistRepository.getChecklist().collect { clients ->
                    val summaries = mutableListOf<ChecklistSummary>()

                    for (client in clients) {
                        try {
                            var vehicleInfo: com.example.checklistdigital.data.VehicleInfo? = null

                            // Get the first (and should be only) VehicleInfo for this client
                            checklistRepository.getVehicleInfoByClientId(client.id).first().let {
                                vehicleInfo = it
                            }

                            if (vehicleInfo != null) {
                                summaries.add(
                                    ChecklistSummary(
                                        clientId = client.id,
                                        insurance = client.insurance,
                                        serviceDate = client.serviceDate,
                                        vehicle = vehicleInfo!!.vehicle,
                                        plate = vehicleInfo!!.plate,
                                        phone = client.phone,
                                        createdAt = client.id.toLong() // Use clientId as proxy for creation time, sorted descending
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            // Skip clients without vehicle info
                            continue
                        }
                    }

                    // Sort checklists from latest (highest ID) to oldest (lowest ID)
                    val sortedSummaries = summaries.sortedByDescending { it.clientId }

                    _uiState.value = _uiState.value.copy(checklists = sortedSummaries, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ocorreu um erro ao carregar os checklists"
                )
            }
        }
    }

    fun selectChecklist(clientId: Int) {
        _uiState.value = _uiState.value.copy(selectedClientId = clientId)
    }

    fun deselectChecklist() {
        _uiState.value = _uiState.value.copy(selectedClientId = null)
    }

    fun deleteChecklist(clientId: Int) {
        viewModelScope.launch {
            try {
                // Fetch the client to delete
                val client = checklistRepository.getClient(clientId).first()

                // Delete the client (cascade delete will handle related records)
                checklistRepository.deleteClient(client)

                // Deselect and reload the list
                deselectChecklist()
                loadChecklists()
            } catch (e: Exception) {
                // Handle error - show error message
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao deletar checklist: ${e.message}",
                    selectedClientId = null
                )
                // Reload to ensure UI is consistent
                loadChecklists()
            }
        }
    }
}
