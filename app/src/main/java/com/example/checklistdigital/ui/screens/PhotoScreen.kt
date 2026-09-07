package com.example.checklistdigital.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.checklistdigital.data.Photo
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider
import com.example.checklistdigital.viewmodel.PhotoViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun PhotoScreen(
    modifier: Modifier = Modifier,
    clientId: Int = -1,
    onBackClick: () -> Unit = {},
    photoViewModel: PhotoViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val uiState = photoViewModel.photoUiState
    val photoUris = photoViewModel.photoUris
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Load existing photos when screen opens
    LaunchedEffect(clientId) {
        if (clientId > 0) {
            photoViewModel.loadPhotos(clientId)
        }
    }

    // Launcher for taking a photo
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoUri?.let { uri ->
                photoViewModel.addPhotoUri(uri)
            }
        }
    }

    // Launcher for requesting camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, open camera
            try {
                val photoUri = createPhotoUri(context)
                currentPhotoUri = photoUri
                takePictureLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Permission denied, show dialog
            showPermissionDialog = true
        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permissão de Câmera") },
            text = { Text("A permissão de câmera é necessária para capturar fotos. Por favor, conceda a permissão nas configurações do aplicativo.") },
            confirmButton = {
                Button(
                    onClick = { showPermissionDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Text(
                text = "Fotos do Cliente nº $clientId",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Error message
            if (uiState.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Loading indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Combined photo display (existing + new)
                val allPhotos = uiState.photos + photoUris.map {
                    Photo(
                        clientId = clientId,
                        photoPath = it.toString()
                    )
                }

                if (allPhotos.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(allPhotos.size) { index ->
                            val photo = allPhotos[index]
                            val isNewPhoto = index >= uiState.photos.size
                            PhotoThumbnail(
                                photo = photo,
                                isNewPhoto = isNewPhoto,
                                onDelete = {
                                    if (isNewPhoto) {
                                        photoViewModel.removePhotoUri(Uri.parse(photo.photoPath))
                                    } else {
                                        coroutineScope.launch {
                                            photoViewModel.deletePhoto(photo)
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma foto adicionada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Add Photo Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            // Request camera permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar Foto",
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Adicionar Foto", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }


            // Action Buttons
            InfoScreenButtons(
                text1 = "Cancelar",
                onBackClick = {
                    photoViewModel.clearPhotoUris()
                    onBackClick()
                },
                text2 = "Salvar",
                nextButtonState = (photoUris.isNotEmpty() || uiState.photos.isNotEmpty()) && !uiState.isSaving,
                onNextClick = {
                    if (clientId > 0) {
                        coroutineScope.launch {
                            // Save new photos from URIs
                            val photoPaths = photoUris.mapIndexed { index, uri ->
                                savePhotoToInternalStorage(context, uri, clientId, index)
                            }

                            val success = photoViewModel.savePhotos(clientId, photoPaths)
                            if (success) {
                                photoViewModel.loadPhotos(clientId)
                                onBackClick()
                            }
                        }
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun PhotoThumbnail(
    photo: Photo,
    isNewPhoto: Boolean = false,
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = photo.photoPath),
            contentDescription = "Photo thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Delete button overlay
    IconButton(
        onClick = onDelete,
        modifier = Modifier
            .padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Deletar Foto",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
        )
    }

    // "New" indicator
    if (isNewPhoto) {
        Card(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(
                text = "Nova",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(4.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



/**
 * Creates a temporary photo file and returns its URI
 */
fun createPhotoUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir = File(context.cacheDir, "photos").apply {
        if (!exists()) {
            mkdirs()
        }
    }
    val image = File.createTempFile(imageFileName, ".jpg", storageDir)

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        image
    )
}

/**
 * Saves photo from cache to internal storage with clientId in the path
 * Uses UUID to ensure unique filenames for multiple photos taken in quick succession
 */
fun savePhotoToInternalStorage(context: Context, photoUri: Uri, clientId: Int, index: Int = 0): String {
    val photoDir = File(context.filesDir, "photos/$clientId").apply {
        if (!exists()) {
            mkdirs()
        }
    }

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val uniqueId = UUID.randomUUID().toString().take(8)
    val photoFile = File(photoDir, "photo_${timeStamp}_${uniqueId}.jpg")

    context.contentResolver.openInputStream(photoUri)?.use { input ->
        photoFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    return photoFile.absolutePath
}