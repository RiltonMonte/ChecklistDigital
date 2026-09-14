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

/**
 * ViewModel responsável por gerenciar as informações do veículo
 * vinculadas a um checklist. Controla o estado da UI, valida entradas
 * e interage com o [ChecklistRepository] para salvar, atualizar ou carregar dados.
 */
class VehicleInfoViewModel(private val checklistRepository: ChecklistRepository) : ViewModel() {

    // Estado atual da UI para informações do veículo
    var vehicleInfoUiState by mutableStateOf(vehicleInfoUiState())
        private set

    // Indica se está em modo edição (true) ou inserção (false)
    var isEditMode by mutableStateOf(false)
        private set

    /**
     * Atualiza o estado da UI com novos detalhes do veículo.
     * Também valida os dados inseridos.
     */
    fun updateUiState(vehicleInfoDetails: VehicleInfoDetails) {
        vehicleInfoUiState =
            vehicleInfoUiState(vehicleInfoDetails = vehicleInfoDetails, isEntryValid = validateInput(vehicleInfoDetails))
    }

    /**
     * Salva as informações do veículo no banco de dados.
     * - Se estiver em modo edição, atualiza o registro existente.
     * - Caso contrário, insere um novo registro.
     */
    suspend fun saveVehicleInfo(clientId: Int) {
        if (validateInput()) {
            if (isEditMode) {
                checklistRepository.updateVehicleInfo(
                    vehicleInfoUiState.vehicleInfoDetails.toVehicleInfo(clientId)
                )
            } else {
                checklistRepository.insertVehicleInfo(
                    vehicleInfoUiState.vehicleInfoDetails.toVehicleInfo(clientId)
                )
            }
        }
    }

    /**
     * Carrega informações de veículo existentes para edição, caso existam.
     * Caso contrário, mantém modo inserção.
     */
    fun loadVehicleInfoForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val vehicleInfo = checklistRepository.getVehicleInfoByClientId(clientId).first()
                isEditMode = true
                updateUiState(vehicleInfo.toVehicleInfoDetails())
            } catch (e: Exception) {
                // Informações de veículo podem não existir
                isEditMode = false
            }
        }
    }

    /**
     * Reseta o estado para criação de um novo veículo.
     */
    fun resetForNewVehicle() {
        isEditMode = false
        vehicleInfoUiState = vehicleInfoUiState()
    }

    /**
     * Valida os campos obrigatórios das informações do veículo.
     */
    fun validateInput(uiState: VehicleInfoDetails = vehicleInfoUiState.vehicleInfoDetails): Boolean {
        return with(uiState) {
            vehicle.isNotBlank() && plate.isNotBlank() && color.isNotBlank() && year.isNotBlank()
        }
    }


}

/**
 * Representa os detalhes de um veículo.
 */
data class VehicleInfoDetails(
    val id: Int = 0,
    val vehicle: String = "",
    val plate: String = "",
    val color: String = "",
    val year: String = "",
)

/**
 * Estado da UI para informações do veículo, incluindo dados e validação.
 */
data class vehicleInfoUiState(
    val vehicleInfoDetails: VehicleInfoDetails = VehicleInfoDetails(),
    val isEntryValid: Boolean = false
)

/**
 * Converte [VehicleInfoDetails] em entidade [VehicleInfo] para persistência.
 */
fun VehicleInfoDetails.toVehicleInfo(clientId: Int): VehicleInfo = VehicleInfo(
    id = id,
    clientId = clientId,
    vehicle = vehicle,
    plate = plate,
    color = color,
    year = year,
)

/**
 * Converte entidade [VehicleInfo] em [VehicleInfoDetails] para uso na UI.
 */
fun VehicleInfo.toVehicleInfoDetails(): VehicleInfoDetails = VehicleInfoDetails(
    id = id,
    vehicle = vehicle,
    plate = plate,
    color = color,
    year = year,
)

/**
 * Converte entidade [VehicleInfo] em [vehicleInfoUiState].
 */
fun VehicleInfo.toVehicleInfoUiState(isEntryValid: Boolean = false): vehicleInfoUiState = vehicleInfoUiState(
    vehicleInfoDetails = this.toVehicleInfoDetails(),
    isEntryValid = isEntryValid
)