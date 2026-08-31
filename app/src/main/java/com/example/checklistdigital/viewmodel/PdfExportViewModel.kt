package com.example.checklistdigital.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.utils.PdfExportUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PdfExportUiState(
    val isExporting: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val filePath: String? = null
)

class PdfExportViewModel(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfExportUiState())
    val uiState: StateFlow<PdfExportUiState> = _uiState.asStateFlow()

    fun exportChecklistToPdf(context: Context, clientId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isExporting = true,
                    error = null,
                    success = false
                )

                // Fetch all data
                val client = checklistRepository.getClient(clientId).first()
                val vehicleInfo = checklistRepository.getVehicleInfoByClientId(clientId).first()
                val address = checklistRepository.getAddress(clientId).first()
                val vehicleStatus1 = checklistRepository.getVehicleStatus1(clientId).first()
                val vehicleStatus2 = checklistRepository.getVehicleStatus2(clientId).first()
                val photos = checklistRepository.getPhotosByClientId(clientId).first()

                // Generate PDF
                val filePath = PdfExportUtils.generateChecklistPdf(
                    context = context,
                    client = client,
                    vehicleInfo = vehicleInfo,
                    address = address,
                    vehicleStatus1 = vehicleStatus1,
                    vehicleStatus2 = vehicleStatus2,
                    photos = photos
                )

                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    success = true,
                    filePath = filePath
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    error = e.message ?: "Erro ao exportar PDF",
                    success = false
                )
                e.printStackTrace()
            }
        }
    }

    fun resetState() {
        _uiState.value = PdfExportUiState()
    }
}
