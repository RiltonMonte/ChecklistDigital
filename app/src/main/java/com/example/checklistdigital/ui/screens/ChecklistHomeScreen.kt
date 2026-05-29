package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.checklistdigital.viewmodel.ChecklistHomeViewModel
import com.example.checklistdigital.viewmodel.ChecklistHomeUiState
import com.example.checklistdigital.viewmodel.ChecklistSummary
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider


@Composable
fun ChecklistHomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    checklistHomeViewModel: ChecklistHomeViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
) {
    val uiState by checklistHomeViewModel.uiState.collectAsState()

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
                                    }
                                )
                            }
                        }

                        // Edit and Delete Buttons
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
                                    onClick = navigateToItemEntry,
                                    icon = { Icon(Icons.Filled.Add, contentDescription = "Novo Checklist") },
                                    text = { Text("Novo Checklist") },
                                    modifier = Modifier
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                ExtendedFloatingActionButton(
                                    onClick = navigateToItemEntry,
                                    icon = {
                                        Icon(
                                            Icons.Filled.Add,
                                            contentDescription = "Novo Checklist"
                                        )
                                    },
                                    text = { Text("Novo Checklist") },
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChecklistCard(
    modifier: Modifier = Modifier,
    checklistSummary: ChecklistSummary,
    isSelected: Boolean = false,
    onCardClick: () -> Unit,

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
            // Client Name and Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = checklistSummary.clientName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = checklistSummary.serviceDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Vehicle Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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

            // Phone
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Telefone: ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = checklistSummary.phone,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun ChecklistHomeScreenPreview() {
    ChecklistHomeScreen(
        navigateToItemEntry = {},
        navigateToItemUpdate = {}
    )
}

@Composable
@Preview(
    showBackground = true
)
fun ChecklistCardPreview() {
    ChecklistCard(
        checklistSummary = ChecklistSummary(
            clientId = 1,
            clientName = "João Silva",
            serviceDate = "15/05/2026",
            vehicle = "Honda Civic",
            plate = "ABC-1234",
            phone = "(11) 99999-8888"
        ),
        isSelected = true,
        onCardClick = {}
    )
}
