package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.checklistdigital.viewmodel.AddressDetails
import com.example.checklistdigital.viewmodel.AddressUiState
import com.example.checklistdigital.viewmodel.AddressViewModel
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider
import kotlinx.coroutines.launch


@Composable
fun AddressInfoScreen(
    onNextClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier,
    addressViewModel: AddressViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier) {
        InfoFields(
            uiState = addressViewModel.addressUiState,
            onValueChange = addressViewModel::updateUiState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        InfoScreenButtons(
            onNextClick = {
               coroutineScope.launch {
                   val clientId = clientViewModel.getLastClientId()
                   addressViewModel.saveAddress(clientId)
                   onNextClick()
               }
            },
            onBackClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }

}

@Composable
fun InfoFields(
    uiState: AddressUiState,
    onValueChange: (AddressDetails) -> Unit = {},
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        Card(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Endereço Origem",
                modifier = Modifier.padding(8.dp)
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.originStreet,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(originStreet = it)) },
                labelName = { Text("Rua/Avenida") },
                modifier = Modifier
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.originNumber,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(originNumber = it)) },
                labelName = { Text("Número") },
                modifier = Modifier
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.originDistrict,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(originDistrict = it)) },
                labelName = { Text("Bairro") },
                modifier = Modifier
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.originCity,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(originCity = it)) },
                labelName = { Text("Cidade") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Card(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Endereço Destino",
                modifier = Modifier.padding(8.dp)
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.destinyStreet,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(destinyStreet = it)) },
                labelName = { Text("Rua/Avenida") },
                modifier = Modifier
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.destinyNumber,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(destinyNumber = it)) },
                labelName = { Text("Número") },
                modifier = Modifier
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.destinyDistrict,
                onInfoInputChange = { onValueChange(uiState.addressDetails.copy(destinyDistrict = it)) },
                labelName = { Text("Bairro") },
                modifier = Modifier
            )
            InfoScreenField(
                infoInput = uiState.addressDetails.destinyCity,
                onInfoInputChange = {
                    onValueChange(uiState.addressDetails.copy(destinyCity = it)) },
                labelName = { Text("Cidade") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

    }
}
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun AddressInfoScreenPreview(){
    AddressInfoScreen(modifier = Modifier)
}