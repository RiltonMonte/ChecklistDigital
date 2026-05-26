package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider
import com.example.checklistdigital.viewmodel.ClientDetails
import com.example.checklistdigital.viewmodel.ClientUiState
import com.example.checklistdigital.viewmodel.ClientViewModel
import com.example.checklistdigital.viewmodel.VehicleInfoDetails
import com.example.checklistdigital.viewmodel.VehicleInfoViewModel
import com.example.checklistdigital.viewmodel.vehicleInfoUiState
import kotlinx.coroutines.launch


@Composable
fun ClientInfoScreen(
    navController: NavHostController,
    onNextClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    backButtonState: Boolean,
    modifier: Modifier,
    clientViewModel: ClientViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
    vehicleInfoViewModel: VehicleInfoViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxHeight()
    ) {
        ClientScreen(
            uiState = clientViewModel.clientUiState,
            onValueChange = clientViewModel::updateUiState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
        VeihicleScreen(
            uiState = vehicleInfoViewModel.vehicleInfoUiState,
            onValueChange = vehicleInfoViewModel::updateUiState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )


        Spacer(modifier = Modifier.weight(1f))
        InfoScreenButtons(
            onNextClick = {
                coroutineScope.launch {
                    //clientViewModel.saveClient()
                    val clientId = clientViewModel.saveClient()
                    if (clientId > 0) {
                        vehicleInfoViewModel.saveVehicleInfo(clientId)
                        navController.currentBackStackEntry?.savedStateHandle?.set("clientId", clientId)
                        onNextClick()
                    }
                }
            },
            onBackClick = onBackClick,
            backButtonState = backButtonState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

    }
}

@Composable
fun ClientScreen(
    uiState: ClientUiState,
    onValueChange: (ClientDetails) -> Unit = {},
    modifier: Modifier,
){
    Card(modifier = Modifier.padding(4.dp)) {
        //TODO: alterar formatação da data
        InfoScreenField(
            infoInput = uiState.clientDetails.serviceDate,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(serviceDate = it)) },
            labelName = { Text("Data") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.clientDetails.clientName,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(clientName = it)) },
            labelName = { Text("Nome do Cliente") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.clientDetails.insurance,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(insurance = it)) },
            labelName = { Text("Seguradora") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.clientDetails.accident,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(accident = it)) },
            labelName = { Text("Sinistro") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.clientDetails.phone,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(phone = it)) },
            labelName = { Text("Telefone") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun VeihicleScreen(
    uiState: vehicleInfoUiState,
    onValueChange: (VehicleInfoDetails) -> Unit = {},
    modifier: Modifier,
){
    Card(modifier = Modifier.padding(4.dp)) {
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.vehicle,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(vehicle = it)) },
            labelName = { Text("Veiculo") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.plate,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(plate = it)) },
            labelName = { Text("Placa") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.color,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(color = it)) },
            labelName = { Text("Cor") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.year,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(year = it)) },
            labelName = { Text("Ano") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

//@Composable
//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//fun ClientInfoScreenPreview(){
//    ClientInfoScreen(
//        backButtonState = false,
//        modifier = Modifier
//    )
//}