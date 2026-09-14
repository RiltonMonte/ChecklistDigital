package com.example.checklistdigital.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklistdigital.data.ChecklistRepository
import com.example.checklistdigital.data.Photo
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por gerenciar as fotos vinculadas a um checklist.
 * Controla o estado da UI, manipula URIs temporárias e interage com o
 * [ChecklistRepository] para salvar, carregar e excluir fotos.
 */
class PhotoViewModel(private val checklistRepository: ChecklistRepository) : ViewModel() {

    // Estado atual da UI para fotos
    var photoUiState by mutableStateOf(PhotoUiState())
        private set

    // Lista de URIs de fotos capturadas mas ainda não salvas
    var photoUris by mutableStateOf<List<Uri>>(emptyList())
        private set

    // URI da foto atualmente em captura
    var currentPhotoUri by mutableStateOf<Uri?>(null)
        private set

    /**
     * Adiciona uma nova URI de foto à lista temporária.
     */
    fun addPhotoUri(uri: Uri) {
        photoUris = photoUris + uri
    }

    /**
     * Remove uma URI de foto da lista temporária.
     */
    fun removePhotoUri(uri: Uri) {
        photoUris = photoUris - uri
    }

    /**
     * Limpa todas as URIs temporárias de fotos.
     */
    fun clearPhotoUris() {
        photoUris = emptyList()
    }

    /**
     * Carrega fotos já salvas no banco de dados para um cliente específico.
     */
    fun loadPhotos(clientId: Int) {
        viewModelScope.launch {
            try {
                photoUiState = photoUiState.copy(isLoading = true)
                checklistRepository.getPhotosByClientId(clientId)
                    .collect { photos ->
                        photoUiState = photoUiState.copy(
                            photos = photos,
                            isLoading = false,
                            error = null
                        )
                    }
            } catch (e: Exception) {
                photoUiState = photoUiState.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao carregar fotos"
                )
            }
        }
    }

    /**
     * Salva fotos capturadas (URIs) no banco de dados.
     * @param clientId ID do cliente ao qual as fotos pertencem.
     * @param photoPaths Caminhos absolutos das fotos salvas em armazenamento interno.
     * @return true se todas as fotos foram salvas com sucesso, false caso contrário.
     */
    suspend fun savePhotos(clientId: Int, photoPaths: List<String>): Boolean {
        return try {
            photoUiState = photoUiState.copy(isSaving = true)

            photoPaths.forEach { photoPath ->
                val photo = Photo(
                    clientId = clientId,
                    photoPath = photoPath,
                    timestamp = System.currentTimeMillis()
                )
                checklistRepository.insertPhoto(photo)
            }

            photoUiState = photoUiState.copy(
                isSaving = false,
                error = null
            )
            clearPhotoUris()
            true
        } catch (e: Exception) {
            photoUiState = photoUiState.copy(
                isSaving = false,
                error = e.message ?: "Erro ao salvar fotos"
            )
            false
        }
    }

    /**
     * Exclui uma foto já salva no banco de dados.
     */
    suspend fun deletePhoto(photo: Photo) {
        try {
            checklistRepository.deletePhoto(photo)
        } catch (e: Exception) {
            photoUiState = photoUiState.copy(
                error = e.message ?: "Erro ao deletar foto"
            )
        }
    }

}

/**
 * Estado da UI para fotos.
 * @param photos Lista de fotos salvas no banco de dados.
 * @param isLoading Indica se as fotos estão sendo carregadas.
 * @param error Mensagem de erro, se houver.
 * @param isSaving Indica se fotos estão sendo salvas.
 */
data class PhotoUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false
)
