package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.VehicleStatus1
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por gerenciar o status dos itens/acessórios do veículo
 * vinculados a um checklist. Controla o estado da UI e interage com o
 * [ChecklistRepository] para salvar, atualizar ou carregar dados.
 */
class VehicleStatus1ViewModel ( private val checklistRepository: ChecklistRepository) : ViewModel() {

    // Estado atual da UI para status do veículo (itens/acessórios)
    var vehicleStatus1UiState by mutableStateOf(VehicleStatus1UiState())
        private set

   // Indica se está em modo edição (true) ou inserção (false)
    var isEditMode by mutableStateOf(false)
        private set

    /**
     * Atualiza o estado da UI com novos detalhes do status do veículo.
     */
    fun updateUiState(vehicleStatus1Details: VehicleStatus1Details) {
        vehicleStatus1UiState =
            VehicleStatus1UiState(vehicleStatus1Details = vehicleStatus1Details)
    }

    /**
     * Salva o status dos itens do veículo no banco de dados.
     * - Se estiver em modo edição, atualiza o registro existente.
     * - Caso contrário, insere um novo registro.
     */
    suspend fun saveVehicleStatus1(clientId: Int) {
        if (isEditMode) {
            checklistRepository.updateVehicleStatus1(
                vehicleStatus1UiState.vehicleStatus1Details.toVehicleStatus1(clientId)
            )
        } else {
            checklistRepository.insertVehicleStatus1(
                vehicleStatus1UiState.vehicleStatus1Details.toVehicleStatus1(clientId)
            )
        }
    }

    /**
     * Carrega status de itens do veículo existentes para edição, caso existam.
     * Caso contrário, mantém modo inserção.
     */
    fun loadVehicleStatus1ForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val vehicleStatus1 = checklistRepository.getVehicleStatus1(clientId).first()
                isEditMode = true
                updateUiState(vehicleStatus1.toVehicleStatus1Details())
            } catch (e: Exception) {
                // Status pode não existir
                isEditMode = false
            }
        }
    }

    /**
     * Reseta o estado para criação de um novo status de veículo.
     */
    fun resetForNewVehicleStatus1() {
        isEditMode = false
        vehicleStatus1UiState = VehicleStatus1UiState()
    }

}

/**
 * Representa os detalhes do status dos itens/acessórios do veículo.
 * Cada campo indica se o item está presente (true) ou ausente (false).
 */
data class VehicleStatus1Details (
    val id: Int = 0,
    val documentos: Boolean = false,
    val extintor: Boolean = false,
    val livreto: Boolean = false,
    val tapetes: Boolean = false,
    val radio: Boolean = false,
    val estepe: Boolean = false,
    val cdPlayer: Boolean = false,
    val acededorDeCigarro: Boolean = false,
    val dvdPlayer: Boolean = false,
    val macaco: Boolean = false,
    val moduloAmplificador: Boolean = false,
    val chaveDeRoda: Boolean = false,
    val frenteCD: Boolean = false,
    val triangulo: Boolean = false,
    val antena: Boolean = false,
    val bateria: Boolean = false,
    val rodaLigaLeve: Boolean = false,
    val pintSujaDif: Boolean = false,
)

/**
 * Estado da UI para status dos itens do veículo.
 */
data class VehicleStatus1UiState(
    val vehicleStatus1Details: VehicleStatus1Details = VehicleStatus1Details(),
    val isEntryValid: Boolean = true
)

/**
 * Converte [VehicleStatus1Details] em entidade [VehicleStatus1] para persistência.
 */
fun VehicleStatus1Details.toVehicleStatus1(clientId: Int): VehicleStatus1 = VehicleStatus1(
    id = id,
    clientId = clientId,
    documentos = documentos,
    extintor = extintor,
    livreto = livreto,
    tapetes = tapetes,
    radio = radio,
    estepe = estepe,
    cdPlayer = cdPlayer,
    acededorDeCigarro = acededorDeCigarro,
    dvdPlayer = dvdPlayer,
    macaco = macaco,
    moduloAmplificador = moduloAmplificador,
    chaveDeRoda = chaveDeRoda,
    frenteCD = frenteCD,
    triangulo = triangulo,
    antena = antena,
    bateria = bateria,
    rodaLigaLeve = rodaLigaLeve,
    pintSujaDif = pintSujaDif,
)

/**
 * Converte entidade [VehicleStatus1] em [VehicleStatus1Details] para uso na UI.
 */
fun VehicleStatus1.toVehicleStatus1Details(): VehicleStatus1Details = VehicleStatus1Details(
    id = id,
    documentos = documentos,
    extintor = extintor,
    livreto = livreto,
    tapetes = tapetes,
    radio = radio,
    estepe = estepe,
    cdPlayer = cdPlayer,
    acededorDeCigarro = acededorDeCigarro,
    dvdPlayer = dvdPlayer,
    macaco = macaco,
    moduloAmplificador = moduloAmplificador,
    chaveDeRoda = chaveDeRoda,
    frenteCD = frenteCD,
    triangulo = triangulo,
    antena = antena,
    bateria = bateria,
    rodaLigaLeve = rodaLigaLeve,
    pintSujaDif = pintSujaDif,
)

/**
 * Converte entidade [VehicleStatus1] em [VehicleStatus1UiState].
 */
fun VehicleStatus1.toVehicleStatus1UiState(isEntryValid: Boolean = false): VehicleStatus1UiState = VehicleStatus1UiState(
    vehicleStatus1Details = this.toVehicleStatus1Details(),
    isEntryValid = isEntryValid
)