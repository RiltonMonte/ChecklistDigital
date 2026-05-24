package com.example.checklistdigital.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.checklistdigital.data.Address
import com.example.checklistdigital.data.ChecklistRepository

class AddressViewModel( private val checklistRepository: ChecklistRepository) : ViewModel() {
    var addressUiState by mutableStateOf(AddressUiState())
        private set

    fun updateUiState(addressDetails: AddressDetails) {
        addressUiState =
            AddressUiState(addressDetails = addressDetails, isEntryValid = validateInput(addressDetails))
    }

    suspend fun saveAddress(clientId: Int) {
        if (validateInput()) {
            checklistRepository.insertAddress(addressUiState.addressDetails.toAddress(clientId))
        }
    }

    private fun validateInput(uiState: AddressDetails = addressUiState.addressDetails): Boolean {
        return with(uiState) {
            originStreet.isNotBlank() && originNumber.isNotBlank() && originDistrict.isNotBlank() && originCity.isNotBlank() &&
                    destinyStreet.isNotBlank() && destinyNumber.isNotBlank() && destinyDistrict.isNotBlank() && destinyCity.isNotBlank()
        }
    }
}

data class AddressDetails(
    val id: Int = 0,
    //endereco origem
    val originStreet: String = "",
    val originNumber: String = "",
    val originDistrict: String = "",
    val originCity: String = "",
    //endereco destino
    val destinyStreet: String = "",
    val destinyNumber: String = "",
    val destinyDistrict: String = "",
    val destinyCity: String = "",
)

data class AddressUiState(
    val addressDetails: AddressDetails = AddressDetails(),
    val isEntryValid: Boolean = false
)

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

fun Address.toAddressUiState(isEntryValid: Boolean = false): AddressUiState = AddressUiState(
    addressDetails = this.toAddressDetails(),
    isEntryValid = isEntryValid
)