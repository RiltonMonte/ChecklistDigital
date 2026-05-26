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
                    val summaries = clients.mapNotNull { client ->
                        val vehicleInfo = checklistRepository.getVehicleInfoByClientId(client.id).let { flow ->
                            var result: com.example.checklistdigital.data.VehicleInfo? = null
                            flow.collect { result = it }
                            result
                        }

                        if (vehicleInfo != null) {
                            ChecklistSummary(
                                clientId = client.id,
                                clientName = client.clientName,
                                serviceDate = client.serviceDate,
                                vehicle = vehicleInfo.vehicle,
                                plate = vehicleInfo.plate,
                                phone = client.phone
                            )
                        } else {
                            null
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
