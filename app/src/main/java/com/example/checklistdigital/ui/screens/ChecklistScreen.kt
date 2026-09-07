package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.checklistdigital.viewmodel.AddressDetails
import com.example.checklistdigital.viewmodel.AddressUiState
import com.example.checklistdigital.viewmodel.AddressViewModel
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider
import com.example.checklistdigital.viewmodel.ClientDetails
import com.example.checklistdigital.viewmodel.ClientUiState
import com.example.checklistdigital.viewmodel.ClientViewModel
import com.example.checklistdigital.viewmodel.VehicleInfoDetails
import com.example.checklistdigital.viewmodel.VehicleInfoViewModel
import com.example.checklistdigital.viewmodel.VehicleStatus1Details
import com.example.checklistdigital.viewmodel.VehicleStatus1UiState
import com.example.checklistdigital.viewmodel.VehicleStatus1ViewModel
import com.example.checklistdigital.viewmodel.VehicleStatus2Details
import com.example.checklistdigital.viewmodel.VehicleStatus2UiState
import com.example.checklistdigital.viewmodel.VehicleStatus2ViewModel
import com.example.checklistdigital.viewmodel.vehicleInfoUiState
import kotlinx.coroutines.launch


@Composable
fun ChecklistScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController  = rememberNavController(),
    clientId: Int = -1,
    onNextClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    clientViewModel: ClientViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
    vehicleInfoViewModel: VehicleInfoViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
    addressViewModel: AddressViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
    vehicleStatus1ViewModel: VehicleStatus1ViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
    vehicleStatus2ViewModel: VehicleStatus2ViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(clientId) {
        if (clientId > 0) {
            clientViewModel.loadClientForEdit(clientId)
            vehicleInfoViewModel.loadVehicleInfoForEdit(clientId)
            addressViewModel.loadAddressForEdit(clientId)
            vehicleStatus1ViewModel.loadVehicleStatus1ForEdit(clientId)
            vehicleStatus2ViewModel.loadVehicleStatus2ForEdit(clientId)
        } else {
            clientViewModel.resetForNewClient()
            vehicleInfoViewModel.resetForNewVehicle()
            addressViewModel.resetForNewAddress()
            vehicleStatus1ViewModel.resetForNewVehicleStatus1()
            vehicleStatus2ViewModel.resetForNewVehicleStatus2()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 120.dp)
    ) {
        item {
            ClientScreen(
                uiState = clientViewModel.clientUiState,
                onValueChange = clientViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            VeihicleScreen(
                uiState = vehicleInfoViewModel.vehicleInfoUiState,
                onValueChange = vehicleInfoViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            InfoFields(
                uiState = addressViewModel.addressUiState,
                onValueChange = addressViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            VehicleStatusScreen1(
                uiState = vehicleStatus1ViewModel.vehicleStatus1UiState,
                onValueChange = vehicleStatus1ViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            VehicleStatusScreen2(
                uiState = vehicleStatus2ViewModel.vehicleStatus2UiState,
                onValueChange = vehicleStatus2ViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            InfoScreenButtons(
                text1 = "Cancelar",
                onBackClick = onBackClick,
                text2 = "Salvar",
                nextButtonState = if (
                    clientViewModel.validateInput() &&
                    vehicleInfoViewModel.validateInput() &&
                    addressViewModel.validateInput() &&
                    vehicleStatus2ViewModel.validateInput()
                )true else false,
                onNextClick = {
                    coroutineScope.launch {
                        val clientId = clientViewModel.saveClient()
                        if (clientId > 0) {
                            vehicleInfoViewModel.saveVehicleInfo(clientId)
                            addressViewModel.saveAddress(clientId)
                            vehicleStatus1ViewModel.saveVehicleStatus1(clientId)
                            vehicleStatus2ViewModel.saveVehicleStatus2(clientId)
                            onNextClick()
                        }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 32.dp, start = 28.dp, end = 28.dp)
            )

            // Extra spacer to ensure keyboard doesn't cover content
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ClientScreen(
    uiState: ClientUiState,
    onValueChange: (ClientDetails) -> Unit = {},
    modifier: Modifier,
){
    Card(modifier = Modifier.padding(4.dp)) {
        Text(
            text = "Cliente",
            modifier = Modifier.padding(8.dp)
        )
        InfoScreenField(
            infoInput = uiState.clientDetails.serviceDate,
            onInfoInputChange = { input->
                val digits = input.filter { it.isDigit() }
                val limited = digits.take(8)
                val formmatedDate = if (limited.length==8){
                    buildString {
                        append(limited.take(2))
                        append("/")
                        append(limited.drop(2).take(2))
                        append("/")
                        append(limited.drop(4))
                    }
                } else {
                    limited
                }
                onValueChange(uiState.clientDetails.copy(serviceDate = formmatedDate)) },
            labelName = { Text("Data") },
            modifier = Modifier
        )
        InfoScreenField(
            infoInput = uiState.clientDetails.clientName,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(clientName = it)) },
            labelName = { Text("Nome") },
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
            onInfoInputChange = { input->
                val digits = input.filter { it.isDigit() }
                val limited = digits.take(11)
                val formmatedPhone = if (limited.length==11){
                    buildString {
                        append("(")
                        append(limited.take(2))
                        append(")")
                        append(limited.drop(2).take(5))
                        append("-")
                        append(limited.drop(7))
                    }
                } else {
                    limited
                }
                onValueChange(uiState.clientDetails.copy(phone = formmatedPhone)) },
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
        Text(
            text = "Veiculo",
            modifier = Modifier.padding(8.dp)
        )
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.vehicle,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(vehicle = it)) },
            labelName = { Text("Modelo") },
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
fun VehicleStatusScreen1(
    uiState: VehicleStatus1UiState,
    onValueChange: (VehicleStatus1Details) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 4.dp)) {
        SwitchRow(
            info1 = "Documentos",
            infoChecked1 = uiState.vehicleStatus1Details.documentos,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(documentos = it)) },
            info2 = "Extintor",
            infoChecked2 = uiState.vehicleStatus1Details.extintor,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(extintor = it)) }
        )
        SwitchRow(
            info1 = "Livreto",
            infoChecked1 = uiState.vehicleStatus1Details.livreto,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(livreto = it)) },
            info2 = "Tapetes",
            infoChecked2 = uiState.vehicleStatus1Details.tapetes,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(tapetes = it)) }
        )
        SwitchRow(
            info1 = "Radio",
            infoChecked1 = uiState.vehicleStatus1Details.radio,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(radio = it)) },
            info2 = "Estepe",
            infoChecked2 = uiState.vehicleStatus1Details.estepe,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(estepe = it)) }
        )
        SwitchRow(
            info1 = "CD Player",
            infoChecked1 = uiState.vehicleStatus1Details.cdPlayer,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(cdPlayer = it)) },
            info2 = "Acendedor de Cigarro",
            infoChecked2 = uiState.vehicleStatus1Details.acededorDeCigarro,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(acededorDeCigarro = it)) }
        )
        SwitchRow(
            info1 = "DVD Player",
            infoChecked1 = uiState.vehicleStatus1Details.dvdPlayer,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(dvdPlayer = it)) },
            info2 = "Macaco",
            infoChecked2 = uiState.vehicleStatus1Details.macaco,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(macaco = it)) }
        )
        SwitchRow(
            info1 = "Modulo/Amplificador",
            infoChecked1 = uiState.vehicleStatus1Details.moduloAmplificador,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(moduloAmplificador = it)) },
            info2 = "Chave de Roda",
            infoChecked2 = uiState.vehicleStatus1Details.chaveDeRoda,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(chaveDeRoda = it)) }
        )
        SwitchRow(
            info1 = "Frente CD",
            infoChecked1 = uiState.vehicleStatus1Details.frenteCD,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(frenteCD = it)) },
            info2 = "Triangulo",
            infoChecked2 = uiState.vehicleStatus1Details.triangulo,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(triangulo = it)) }
        )
        SwitchRow(
            info1 = "Antena",
            infoChecked1 = uiState.vehicleStatus1Details.antena,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(antena = it)) },
            info2 = "Bateria",
            infoChecked2 = uiState.vehicleStatus1Details.bateria,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(bateria = it)) }
        )
        SwitchRow(
            info1 = "Roda Liga Leve",
            infoChecked1 = uiState.vehicleStatus1Details.rodaLigaLeve,
            onInfoChecked1Change = { onValueChange(uiState.vehicleStatus1Details.copy(rodaLigaLeve = it)) },
            info2 = "Pint. Suja Dif. Vistoria",
            infoChecked2 = uiState.vehicleStatus1Details.pintSujaDif,
            onInfoChecked2Change = { onValueChange(uiState.vehicleStatus1Details.copy(pintSujaDif = it)) }
        )
    }
}

@Composable
fun VehicleStatusScreen2(
    modifier: Modifier = Modifier,
    onValueChange: (VehicleStatus2Details) -> Unit = {},
    uiState: VehicleStatus2UiState,
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Pneus Dianteiros",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(.30f)
            )
            Card(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            ) {
                ButtonSelection(
                    radioOptions = listOf("Novos", "Bons", "Ruins"),
                    vehicleDetails2 = uiState.vehicleStatus2Details.pneusDianteiros,
                    onClickChange = { onValueChange(uiState.vehicleStatus2Details.copy(pneusDianteiros = it)) },
                    modifier = Modifier
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Pneus Traseiros",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(.30f)
            )
            Card(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            ) {
                ButtonSelection(
                    radioOptions = listOf("Novos", "Bons", "Ruins"),
                    vehicleDetails2 = uiState.vehicleStatus2Details.pneusTraseiros,
                    onClickChange = { onValueChange(uiState.vehicleStatus2Details.copy(pneusTraseiros = it)) },
                    modifier = Modifier
                )
            }

        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Estepe",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(.30f)
            )
            Card(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            ) {
                ButtonSelection(
                    radioOptions = listOf("Novos", "Bons", "Ruins"),
                    vehicleDetails2 = uiState.vehicleStatus2Details.estepe,
                    onClickChange = { onValueChange(uiState.vehicleStatus2Details.copy(estepe = it))},
                    modifier = Modifier
                )
            }
        }
        //marcador de nivel de combustivel
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 24.dp)
        ) {
            Text(text = "Nivel de Combustível: ${(uiState.vehicleStatus2Details.nivelCombustivel * 100).toInt()} %")
            Slider(
                value = uiState.vehicleStatus2Details.nivelCombustivel,
                onValueChange = { onValueChange(uiState.vehicleStatus2Details.copy(nivelCombustivel = it)) }
            )
        }
        //campo de texto para Observações
        Column(modifier = Modifier) {
            OutlinedTextField(
                value = uiState.vehicleStatus2Details.observacoes,
                singleLine = false,
                onValueChange = { onValueChange(uiState.vehicleStatus2Details.copy( observacoes = it )) },
                minLines = 5,
                maxLines = 15,
                label = { Text("Observações") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun ChecklistScreenPreview(){
    ChecklistScreen(
        modifier = Modifier
    )
}