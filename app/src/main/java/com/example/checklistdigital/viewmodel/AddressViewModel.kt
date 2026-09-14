package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.Address
import com.example.checklistdigital.data.ChecklistRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por gerenciar os dados de endereço
 * (origem e destino) vinculados a um checklist.
 *
 * Controla o estado da UI, valida entradas e interage com o
 * [ChecklistRepository] para salvar, atualizar ou carregar endereços.
 */
class AddressViewModel( private val checklistRepository: ChecklistRepository) : ViewModel() {

    // Estado atual da UI para endereço
    var addressUiState by mutableStateOf(AddressUiState())
        private set

    // Indica se está em modo edição (true) ou inserção (false)
    var isEditMode by mutableStateOf(false)
        private set

    /**
     * Atualiza o estado da UI com novos detalhes de endereço.
     * Também valida os dados inseridos.
     */
    fun updateUiState(addressDetails: AddressDetails) {
        addressUiState =
            AddressUiState(addressDetails = addressDetails, isEntryValid = validateInput(addressDetails))
    }

    /**
     * Salva o endereço no banco de dados.
     * - Se estiver em modo edição, atualiza o registro existente.
     * - Caso contrário, insere um novo registro.
     */
    suspend fun saveAddress(clientId: Int) {
        if (validateInput()) {
            if (isEditMode) {
                // Update existing address
                checklistRepository.updateAddress(addressUiState.addressDetails.toAddress(clientId))
            } else {
                // Insert new address
                checklistRepository.insertAddress(addressUiState.addressDetails.toAddress(clientId))
            }
        }
    }

    /**
     * Carrega endereço existente para edição, caso exista.
     * Caso contrário, mantém modo inserção.
     */
    fun loadAddressForEdit(clientId: Int) {
        viewModelScope.launch {
            try {
                val address = checklistRepository.getAddress(clientId).first()
                isEditMode = true
                updateUiState(address.toAddressDetails())
            } catch (e: Exception) {
                // Endereço pode não existir
                isEditMode = false
            }
        }
    }

    /**
     * Reseta o estado para criação de um novo endereço.
     */
    fun resetForNewAddress() {
        isEditMode = false
        addressUiState = AddressUiState()
    }

    /**
     * Valida os campos obrigatórios do endereço.
     */
    fun validateInput(uiState: AddressDetails = addressUiState.addressDetails): Boolean {
        return with(uiState) {
            originStreet.isNotBlank() && originNumber.isNotBlank() && originDistrict.isNotBlank() && originCity.isNotBlank() &&
                    destinyStreet.isNotBlank() && destinyNumber.isNotBlank() && destinyDistrict.isNotBlank() && destinyCity.isNotBlank()
        }
    }
}

/**
 * Representa os detalhes de um endereço (origem e destino).
 */
data class AddressDetails(
    val id: Int = 0,
    // Endereço de origem
    val originStreet: String = "",
    val originNumber: String = "",
    val originDistrict: String = "",
    val originCity: String = "",
    // Endereço de destino
    val destinyStreet: String = "",
    val destinyNumber: String = "",
    val destinyDistrict: String = "",
    val destinyCity: String = "",
)

/**
 * Estado da UI para endereço, incluindo dados e validação.
 */
data class AddressUiState(
    val addressDetails: AddressDetails = AddressDetails(),
    val isEntryValid: Boolean = false
)

/**
 * Converte [AddressDetails] em entidade [Address] para persistência.
 */
fun AddressDetails.toAddress(clientId: Int): Address = Address(
    id = id,
    clientId = clientId,
    originStreet = originStreet,
    originNumber = originNumber,
    originDistrict = originDistrict,
    originCity = originCity,
    destinyStreet = destinyStreet,
    destinyNumber = destinyNumber,
    destinyDistrict = destinyDistrict,
    destinyCity = destinyCity
)

/**
 * Converte entidade [Address] em [AddressDetails] para uso na UI.
 */
fun Address.toAddressDetails(): AddressDetails = AddressDetails(
    id = id,
    originStreet = originStreet,
    originNumber = originNumber,
    originDistrict = originDistrict,
    originCity = originCity,
    destinyStreet = destinyStreet,
    destinyNumber = destinyNumber,
    destinyDistrict = destinyDistrict,
    destinyCity = destinyCity
)

/**
 * Converte entidade [Address] em [AddressUiState].
 */
fun Address.toAddressUiState(isEntryValid: Boolean = false): AddressUiState = AddressUiState(
    addressDetails = this.toAddressDetails(),
    isEntryValid = isEntryValid
)