package com.example.checklistdigital.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.utils.PdfExportUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Estado da UI para exportação de checklist em PDF.
 *
 * @param isExporting Indica se a exportação está em andamento.
 * @param error Mensagem de erro, se houver.
 * @param success Indica se a exportação foi concluída com sucesso.
 * @param filePath Caminho do arquivo PDF gerado.
 */
data class PdfExportUiState(
    val isExporting: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val filePath: String? = null
)

/**
 * ViewModel responsável por gerenciar a exportação de checklists em PDF.
 * Controla o estado da UI durante o processo de exportação e interage
 * com o [ChecklistRepository] para obter os dados necessários.
 */
class PdfExportViewModel(
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfExportUiState())
    val uiState: StateFlow<PdfExportUiState> = _uiState.asStateFlow()

    /**
     * Exporta um checklist para PDF.
     * - Busca todos os dados relacionados ao cliente (cliente, veículo, endereço, status, fotos).
     * - Gera o PDF usando [PdfExportUtils].
     * - Atualiza o estado da UI com sucesso ou erro.
     * @param context Contexto da aplicação, usado para acessar recursos e diretórios.
     * @param clientId ID do cliente cujo checklist será exportado.
     */
    fun exportChecklistToPdf(context: Context, clientId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isExporting = true,
                    error = null,
                    success = false
                )

                // Busca todos os dados necessários
                val client = checklistRepository.getClient(clientId).first()
                val vehicleInfo = checklistRepository.getVehicleInfoByClientId(clientId).first()
                val address = checklistRepository.getAddress(clientId).first()
                val vehicleStatus1 = checklistRepository.getVehicleStatus1(clientId).first()
                val vehicleStatus2 = checklistRepository.getVehicleStatus2(clientId).first()
                val photos = checklistRepository.getPhotosByClientId(clientId).first()

                // Gera o PDF
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

    /**
     * Reseta o estado da UI para valores iniciais.
     * Usado após concluir ou cancelar uma exportação.
     */
    fun resetState() {
        _uiState.value = PdfExportUiState()
    }
}
