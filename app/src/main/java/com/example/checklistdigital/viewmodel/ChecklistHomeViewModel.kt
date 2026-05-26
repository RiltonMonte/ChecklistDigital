package com.example.checklistdigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

data class ChecklistSummary(
    val clientId: Int,
    val clientName: String,
    val serviceDate: String,
    val vehicle: String,
    val plate: String,
    val phone: String
)

data class ChecklistHomeUiState(
    val checklists: List<ChecklistSummary> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
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
                                        clientName = client.clientName,
                                        serviceDate = client.serviceDate,
                                        vehicle = vehicleInfo!!.vehicle,
                                        plate = vehicleInfo!!.plate,
                                        phone = client.phone
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            // Skip clients without vehicle info
                            continue
                        }
                    }

                    _uiState.value = _uiState.value.copy(checklists = summaries, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ocorreu um erro ao carregar os checklists"
                )
            }
        }
    }

    fun deleteChecklist(clientId: Int) {
        // TODO: Implementar função deletar checklist
    }
}
