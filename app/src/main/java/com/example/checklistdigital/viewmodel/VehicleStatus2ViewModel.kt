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

/**
 * ViewModel responsável por gerenciar o status dos pneus, nível de combustível
 * e observações vinculados a um checklist. Controla o estado da UI, valida entradas
 * e interage com o [ChecklistRepository] para salvar, atualizar ou carregar dados.
 */
class VehicleStatus2ViewModel (private val checklistRepository: ChecklistRepository) : ViewModel() {

    // Estado atual da UI para status do veículo (pneus/combustível/observações)
    var vehicleStatus2UiState by mutableStateOf(VehicleStatus2UiState())
        private set

    // Indica se está em modo edição (true) ou inserção (false)
    var isEditMode by mutableStateOf(false)
        private set

    /**
     * Atualiza o estado da UI com novos detalhes do status do veículo.
     * Também valida os dados inseridos.
     */
    fun updateUiState(vehicleStatus2Details: VehicleStatus2Details) {
        vehicleStatus2UiState =
            VehicleStatus2UiState(vehicleStatus2Details = vehicleStatus2Details, isEntryValid = validateInput(vehicleStatus2Details))
    }

    /**
     * Salva o status dos pneus/combustível/observações no banco de dados.
     * - Se estiver em modo edição, atualiza o registro existente.
     * - Caso contrário, insere um novo registro.
     */
    suspend fun saveVehicleStatus2(clientId: Int) {
        if (validateInput()) {
            if (isEditMode) {
                checklistRepository.updateVehicleStatus2(
                    vehicleStatus2UiState.vehicleStatus2Details.toVehicleStatus2(clientId)
                )
            } else {
                checklistRepository.insertVehicleStatus2(
                    vehicleStatus2UiState.vehicleStatus2Details.toVehicleStatus2(clientId)
                )
            }
        }
    }

    /**
     * Carrega status de pneus/combustível/observações existentes para edição, caso existam.
     * Caso contrário, mantém modo inserção.
     */
    fun loadVehicleStatus2ForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val vehicleStatus2 = checklistRepository.getVehicleStatus2(clientId).first()
                isEditMode = true
                updateUiState(vehicleStatus2.toVehicleStatus2Details())
            } catch (e: Exception) {
                // Status pode não existir
                isEditMode = false
            }
        }
    }

    /**
     * Reseta o estado para criação de um novo status de veículo.
     */
    fun resetForNewVehicleStatus2() {
        isEditMode = false
        vehicleStatus2UiState = VehicleStatus2UiState()
    }

    /**
     * Valida os campos obrigatórios do status do veículo.
     * Neste caso, exige que o campo de observações não esteja vazio.
     */
    fun validateInput(uiState: VehicleStatus2Details = vehicleStatus2UiState.vehicleStatus2Details): Boolean {
        return with(uiState) {
            observacoes.isNotBlank()
        }
    }
}

/**
 * Representa os detalhes do status do veículo (pneus, combustível e observações).
 */
data class VehicleStatus2Details (
    val id: Int = 0,
    val pneusDianteiros: Int = 0, // 0 = Novos, 1 = Bons, 2 = Ruins
    val pneusTraseiros: Int = 0, // 0 = Novos, 1 = Bons, 2 = Ruins
    val estepe: Int = 0, // 0 = Novos, 1 = Bons, 2 = Ruins
    val nivelCombustivel: Float = .0f, // Valor entre 0.0 e 1.0 representando porcentagem
    val observacoes: String = "",
)

/**
 * Estado da UI para status do veículo (pneus/combustível/observações).
 */
data class VehicleStatus2UiState(
    val vehicleStatus2Details: VehicleStatus2Details = VehicleStatus2Details(),
    val isEntryValid: Boolean = false
)

/**
 * Converte [VehicleStatus2Details] em entidade [VehicleStatus2] para persistência.
 */
fun VehicleStatus2Details.toVehicleStatus2(clientId: Int): VehicleStatus2 = VehicleStatus2(
    id = id,
    clientId = clientId,
    pneusDianteiros = pneusDianteiros,
    pneusTraseiros = pneusTraseiros,
    estepe = estepe,
    nivelCombustivel = nivelCombustivel,
    observacoes = observacoes
)

/**
 * Converte entidade [VehicleStatus2] em [VehicleStatus2Details] para uso na UI.
 */
fun VehicleStatus2.toVehicleStatus2Details(): VehicleStatus2Details = VehicleStatus2Details(
    id = id,
    pneusDianteiros = pneusDianteiros,
    pneusTraseiros = pneusTraseiros,
    estepe = estepe,
    nivelCombustivel = nivelCombustivel,
    observacoes = observacoes
)

/**
 * Converte entidade [VehicleStatus2] em [VehicleStatus2UiState].
 */
fun VehicleStatus2.toVehicleStatus2UiState(isEntryValid: Boolean = false): VehicleStatus2UiState = VehicleStatus2UiState(
    vehicleStatus2Details = this.toVehicleStatus2Details(),
    isEntryValid = isEntryValid
)