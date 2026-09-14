package com.example.checklistdigital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Representa um resumo de checklist para exibição na tela inicial.
 *
 * @param clientId ID do cliente associado ao checklist.
 * @param insurance Nome da seguradora.
 * @param serviceDate Data do serviço.
 * @param vehicle Modelo do veículo.
 * @param plate Placa do veículo.
 * @param phone Telefone do cliente.
 * @param createdAt Timestamp de criação (usado para ordenação).
 */
data class ChecklistSummary(
    val clientId: Int,
    val insurance: String,
    val serviceDate: String,
    val vehicle: String,
    val plate: String,
    val phone: String,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Estado da UI da tela inicial de checklists.
 *
 * @param checklists Lista de checklists resumidos.
 * @param isLoading Indica se os dados estão sendo carregados.
 * @param error Mensagem de erro, se houver.
 * @param selectedClientId ID do checklist selecionado (para ações como editar/deletar).
 */
data class ChecklistHomeUiState(
    val checklists: List<ChecklistSummary> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedClientId: Int? = null  // Track selected checklist
)

/**
 * ViewModel responsável por gerenciar a tela inicial de checklists.
 * Carrega, seleciona, desseleciona e exclui checklists, mantendo o estado da UI.
 */
class ChecklistHomeViewModel(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChecklistHomeUiState())
    val uiState: StateFlow<ChecklistHomeUiState> = _uiState.asStateFlow()

    init {
        loadChecklists()
    }

    /**
     * Carrega todos os checklists do repositório e atualiza o estado da UI.
     * - Ordena os checklists do mais recente para o mais antigo.
     * - Ignora clientes sem informações de veículo.
     */
    fun loadChecklists() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                checklistRepository.getChecklist().collect { clients ->
                    val summaries = mutableListOf<ChecklistSummary>()

                    for (client in clients) {
                        try {
                            var vehicleInfo: com.example.checklistdigital.data.VehicleInfo? = null

                            // Obtém informações de veículo vinculadas ao cliente
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
                                        createdAt = client.id.toLong() // Usa clientId como proxy de ordenação
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            // Ignora clientes sem informações de veículo
                            continue
                        }
                    }

                    // Ordena checklists por ID decrescente (mais recentes primeiro)
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

    /**
     * Seleciona um checklist pelo ID do cliente.
     */
    fun selectChecklist(clientId: Int) {
        _uiState.value = _uiState.value.copy(selectedClientId = clientId)
    }

    /**
     * Remove a seleção de checklist.
     */
    fun deselectChecklist() {
        _uiState.value = _uiState.value.copy(selectedClientId = null)
    }

    /**
     * Exclui um checklist e seus dados relacionados.
     * - Busca o cliente pelo ID.
     * - Exclui o cliente (com deleção em cascata dos dados relacionados).
     * - Atualiza a lista de checklists.
     */
    fun deleteChecklist(clientId: Int) {
        viewModelScope.launch {
            try {
                val client = checklistRepository.getClient(clientId).first()
                checklistRepository.deleteClient(client)

                deselectChecklist()
                loadChecklists()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao deletar checklist: ${e.message}",
                    selectedClientId = null
                )
                loadChecklists()
            }
        }
    }
}
