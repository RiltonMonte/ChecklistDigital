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

/**
 * ViewModel responsável por gerenciar os dados do cliente
 * vinculados a um checklist. Controla o estado da UI, valida
 * entradas e interage com o [ChecklistRepository] para salvar,
 * atualizar ou carregar clientes.
 */
class ClientViewModel (private val checklistRepository: ChecklistRepository) : ViewModel() {

    // Estado atual da UI para cliente
    var clientUiState by mutableStateOf(ClientUiState())
        private set

    // Indica se está em modo edição (true) ou inserção (false)
    var isEditMode by mutableStateOf(false)
        private set

    // ID do cliente atualmente sendo manipulado
    var currentClientId by mutableStateOf(-1)
        private set

    /**
     * Atualiza o estado da UI com novos detalhes do cliente.
     * Também valida os dados inseridos.
     */
    fun updateUiState(clientDetails: ClientDetails) {
        clientUiState =
            ClientUiState(clientDetails = clientDetails, isEntryValid = validateInput(clientDetails))
    }

    /**
     * Salva o cliente no banco de dados.
     * - Se estiver em modo edição, atualiza o registro existente.
     * - Caso contrário, insere um novo registro e retorna o novo ID.
     * @return ID do cliente salvo ou -1 se inválido.
     */
    suspend fun saveClient(): Int {
        return if (validateInput()) {
            if (isEditMode) {
                checklistRepository.updateClient(clientUiState.clientDetails.toClient())
                clientUiState.clientDetails.id
            } else {
                val newId = checklistRepository.insertClient(clientUiState.clientDetails.toClient()).toInt()
                currentClientId = newId
                newId
            }
        } else {
            -1
        }
    }

    /**
     * Carrega cliente existente para edição, caso exista.
     * Caso contrário, mantém modo inserção.
     */
    fun loadClientForEdit(clientId: Int) {
        currentClientId = clientId
        viewModelScope.launch {
            try {
                val client = checklistRepository.getClient(clientId).first()
                isEditMode = true
                updateUiState(client.toClientDetails())
            } catch (e: Exception) {
                // Cliente pode não existir
                isEditMode = false
            }
        }
    }

    /**
     * Reseta o estado para criação de um novo cliente.
     */
    fun resetForNewClient() {
        isEditMode = false
        currentClientId = -1
        clientUiState = ClientUiState()
    }

    /**
     * Valida os campos obrigatórios do cliente.
     */
    fun validateInput(uiState: ClientDetails = clientUiState.clientDetails): Boolean {
        return with(uiState) {
            serviceDate.isNotBlank() && clientName.isNotBlank() && insurance.isNotBlank() && accident.isNotBlank() && phone.isNotBlank()
        }
    }
}

/**
 * Representa os detalhes de um cliente.
 */
data class ClientDetails(
    val id: Int = 0,
    val serviceDate: String = "",
    val clientName: String = "",
    val insurance: String = "",
    val accident: String = "",
    val phone: String = "",
)

/**
 * Estado da UI para cliente, incluindo dados e validação.
 */
data class ClientUiState(
    val clientDetails: ClientDetails = ClientDetails(),
    val isEntryValid: Boolean = false
)

/**
 * Converte [ClientDetails] em entidade [Client] para persistência.
 */
fun ClientDetails.toClient(): Client = Client(
    id = id,
    serviceDate = serviceDate,
    clientName = clientName,
    insurance = insurance,
    accident = accident,
    phone = phone,
)

/**
 * Converte entidade [Client] em [ClientDetails] para uso na UI.
 */
fun Client.toClientDetails(): ClientDetails = ClientDetails(
    id = id,
    serviceDate = serviceDate,
    clientName = clientName,
    insurance = insurance,
    accident = accident,
    phone = phone,
)

/**
 * Converte entidade [Client] em [ClientUiState].
 */
fun Client.toClientUiState(isEntryValid: Boolean = false): ClientUiState = ClientUiState(
    clientDetails = this.toClientDetails(),
    isEntryValid = isEntryValid
)