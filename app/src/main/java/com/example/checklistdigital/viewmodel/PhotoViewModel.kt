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

class PhotoViewModel(private val checklistRepository: ChecklistRepository) : ViewModel() {
    var photoUiState by mutableStateOf(PhotoUiState())
        private set

    var photoUris by mutableStateOf<List<Uri>>(emptyList())
        private set

    var currentPhotoUri by mutableStateOf<Uri?>(null)
        private set


    fun addPhotoUri(uri: Uri) {
        photoUris = photoUris + uri
    }

    fun removePhotoUri(uri: Uri) {
        photoUris = photoUris - uri
    }

    fun clearPhotoUris() {
        photoUris = emptyList()
    }

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

data class PhotoUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false
)
