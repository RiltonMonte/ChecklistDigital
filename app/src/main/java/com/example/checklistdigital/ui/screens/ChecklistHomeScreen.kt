package com.example.checklistdigital.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.checklistdigital.R
import com.example.checklistdigital.viewmodel.ChecklistHomeViewModel
import com.example.checklistdigital.viewmodel.ChecklistSummary
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider
import com.example.checklistdigital.viewmodel.PdfExportViewModel
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun ChecklistHomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    navigateToPhoto: (Int) -> Unit,
    modifier: Modifier = Modifier,
    checklistHomeViewModel: ChecklistHomeViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
    pdfExportViewModel: PdfExportViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
) {
    val uiState by checklistHomeViewModel.uiState.collectAsState()
    val pdfUiState = pdfExportViewModel.uiState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showExportDialog by remember { mutableStateOf(false) }
    var selectedClientForExport by remember { mutableStateOf<Int?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        checklistHomeViewModel.deselectChecklist()
    }

    //Exportar PDF
    // Success dialog
    if (pdfUiState.success && pdfUiState.filePath != null) {
        AlertDialog(
            onDismissRequest = { pdfExportViewModel.resetState() },
            title = { Text("PDF Exportado com Sucesso") },
            text = { Text("O checklist foi exportado como PDF.\n\nCaminho: ${pdfUiState.filePath}") },
            confirmButton = {
                Button(
                    onClick = {
                        // Share the PDF
                        val file = File(pdfUiState.filePath)
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Compartilhar PDF"))
                        pdfExportViewModel.resetState()
                    }
                ) {
                    Text("Compartilhar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { pdfExportViewModel.resetState() }
                ) {
                    Text("Fechar")
                }
            }
        )
    }

    // Error dialog
    if (pdfUiState.error != null) {
        AlertDialog(
            onDismissRequest = { pdfExportViewModel.resetState() },
            title = { Text("Erro ao Exportar") },
            text = { Text(pdfUiState.error ?: "Erro desconhecido") },
            confirmButton = {
                Button(
                    onClick = { pdfExportViewModel.resetState() }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Text(
                text = "Meus Checklists",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Content
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Erro: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                uiState.checklists.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Nenhum checklist criado",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Clique no botão + para criar um novo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                else ->{
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                top = 8.dp,
                                bottom = 8.dp
                            )
                        ) {
                            items(uiState.checklists) { checklistSummary ->
                                ChecklistCard(
                                    checklistSummary = checklistSummary,
                                    isSelected = uiState.selectedClientId == checklistSummary.clientId,
                                    onCardClick = {
                                        if (uiState.selectedClientId == checklistSummary.clientId) {
                                            checklistHomeViewModel.deselectChecklist()
                                        } else {
                                            checklistHomeViewModel.selectChecklist(checklistSummary.clientId)
                                        }
                                    },
                                    navigateToPhoto = { clientId ->
                                        navigateToPhoto(clientId)
                                    },
                                    onExportPdf = { clientId ->
                                        selectedClientForExport = clientId
                                        showExportDialog = true
                                    },
                                    isExportingPdf = pdfUiState.isExporting
                                )
                            }
                        }

                        if (uiState.selectedClientId != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                ExtendedFloatingActionButton(
                                    onClick = {
                                        navigateToItemUpdate(uiState.selectedClientId!!)
                                        checklistHomeViewModel.deselectChecklist()
                                    },
                                    modifier = Modifier,
                                    icon = { Icon(Icons.Filled.Edit, contentDescription = "Editar") },
                                    text = { Text("Editar") }
                                )

                                ExtendedFloatingActionButton(
                                    onClick = {
                                        checklistHomeViewModel.deleteChecklist(uiState.selectedClientId!!)
                                    },
                                    modifier = Modifier,
                                    icon = { Icon(Icons.Filled.Delete, contentDescription = "Deletar") },
                                    text = { Text("Deletar") },
                                    containerColor = MaterialTheme.colorScheme.error
                                )

                                ExtendedFloatingActionButton(
                                    onClick = {
                                        selectedClientForExport = uiState.selectedClientId
                                        showExportDialog = true
                                    },
                                    //enabled = !pdfUiState.isExporting,
                                    modifier = Modifier,
                                    icon = { Icon(Icons.Filled.FileDownload, contentDescription = "Exportar PDF") },
                                    text = {
                                        if (pdfUiState.isExporting) {
                                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                        } else {
                                            Text("Exportar PDF")
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(56.dp))

                            }
                        }
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = navigateToItemEntry,
            icon = { Icon(Icons.Filled.Add, contentDescription = "Novo Checklist") },
            text = { Text("Novo Checklist") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
        )
    }

    // Export confirmation dialog
    if (showExportDialog && selectedClientForExport != null) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Exportar para PDF?") },
            text = { Text("Deseja exportar este checklist como PDF? O arquivo será salvo e você poderá compartilhá-lo.") },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pdfExportViewModel.exportChecklistToPdf(context, selectedClientForExport!!)
                        }
                        showExportDialog = false
                    }
                ) {
                    Text("Exportar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showExportDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun ChecklistCard(
    modifier: Modifier = Modifier,
    checklistSummary: ChecklistSummary,
    isSelected: Boolean = false,
    onCardClick: () -> Unit,
    navigateToPhoto: (Int) -> Unit = {},
    onExportPdf: (Int) -> Unit = {},
    isExportingPdf: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.outlineVariant
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Data e Seguradora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Seguradora",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = checklistSummary.insurance,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                }
                Column {
                    Text(
                        text = "Data",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = checklistSummary.serviceDate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Veiculo e Placa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column {
                    Row(
                        modifier = Modifier
                    ) {
                        Column {
                            Text(
                                text = "Veículo",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = checklistSummary.vehicle,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                    ) {
                        Column {
                            Text(
                                text = "Placa",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = checklistSummary.plate,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                //Buttons Row
                Row() {
                    Spacer(modifier = Modifier.width(10.dp))
                    // Photo button
                    OutlinedIconButton(
                        onClick = { navigateToPhoto(checklistSummary.clientId) },
                        modifier = Modifier.width(52.dp).height(52.dp),
                        enabled = !isExportingPdf
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_a_photo_24px),
                            contentDescription = "Adicionar Foto",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Export PDF button
                    OutlinedIconButton(
                        onClick = { onExportPdf(checklistSummary.clientId) },
                        modifier = Modifier.width(52.dp).height(52.dp),
                        enabled = !isExportingPdf
                    ) {
                        if (isExportingPdf) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.FileDownload,
                                contentDescription = "Exportar PDF",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChecklistCardPreview() {
    ChecklistCard(
        checklistSummary = ChecklistSummary(
            clientId = 1,
            insurance = "Bradesco",
            serviceDate = "15/05/2026",
            vehicle = "Honda Civic",
            plate = "ABC-1234",
            phone = "(11) 99999-8888"
        ),
        isSelected = true,
        onCardClick = {}
    )
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun ChecklistHomeScreenPreview() {
    ChecklistHomeScreen(
        navigateToItemEntry = {},
        navigateToItemUpdate = {},
        navigateToPhoto = {}
    )
}
