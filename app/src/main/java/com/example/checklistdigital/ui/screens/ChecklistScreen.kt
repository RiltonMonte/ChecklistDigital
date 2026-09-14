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

/**
 * Tela principal de preenchimento do checklist.
 * Exibe seções para dados do cliente, veículo, endereço e status do veículo,
 * permitindo salvar ou cancelar o checklist.
 *
 * @param modifier Permite aplicar modificadores de layout.
 * @param navController Controlador de navegação (usado para transições entre telas).
 * @param clientId ID do cliente (se > 0, carrega dados existentes para edição).
 * @param onNextClick Callback chamado ao salvar checklist com sucesso.
 * @param onBackClick Callback chamado ao cancelar preenchimento.
 * @param clientViewModel ViewModel responsável pelos dados do cliente.
 * @param vehicleInfoViewModel ViewModel responsável pelas informações do veículo.
 * @param addressViewModel ViewModel responsável pelo endereço.
 * @param vehicleStatus1ViewModel ViewModel responsável pelo status de itens do veículo.
 * @param vehicleStatus2ViewModel ViewModel responsável pelo status de pneus/combustível.
 */
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

    // Carrega dados existentes ou inicializa novos registros
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

    // Layout principal em lista rolável
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 120.dp)
    ) {
        item {
            // Seção Cliente
            ClientScreen(
                uiState = clientViewModel.clientUiState,
                onValueChange = clientViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            // Seção Veículo
            VeihicleScreen(
                uiState = vehicleInfoViewModel.vehicleInfoUiState,
                onValueChange = vehicleInfoViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            // Seção Endereço
            InfoFields(
                uiState = addressViewModel.addressUiState,
                onValueChange = addressViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            // Seção Status do Veículo (Itens)
            VehicleStatusScreen1(
                uiState = vehicleStatus1ViewModel.vehicleStatus1UiState,
                onValueChange = vehicleStatus1ViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            // Seção Status do Veículo (Pneus/Combustível)
            VehicleStatusScreen2(
                uiState = vehicleStatus2ViewModel.vehicleStatus2UiState,
                onValueChange = vehicleStatus2ViewModel::updateUiState,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            // Botões de ação (Cancelar / Salvar)
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

            // Espaço extra para evitar que o teclado cubra conteúdo
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

/**
 * Seção de preenchimento dos dados do cliente.
 *
 * @param uiState Estado atual dos dados do cliente.
 * @param onValueChange Callback chamado ao alterar algum campo.
 * @param modifier Permite aplicar modificadores de layout.
 */
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

        // Campo Data (formatado como dd/MM/yyyy)
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

        // Campo Nome
        InfoScreenField(
            infoInput = uiState.clientDetails.clientName,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(clientName = it)) },
            labelName = { Text("Nome") },
            modifier = Modifier
        )

        // Campo Seguradora
        InfoScreenField(
            infoInput = uiState.clientDetails.insurance,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(insurance = it)) },
            labelName = { Text("Seguradora") },
            modifier = Modifier
        )

        // Campo Sinistro
        InfoScreenField(
            infoInput = uiState.clientDetails.accident,
            onInfoInputChange = { onValueChange(uiState.clientDetails.copy(accident = it)) },
            labelName = { Text("Sinistro") },
            modifier = Modifier
        )

        // Campo Telefone (formatado como (XX)XXXXX-XXXX)
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

/**
 * Seção de preenchimento das informações do veículo.
 *
 * @param uiState Estado atual dos dados do veículo.
 * @param onValueChange Callback chamado ao alterar algum campo.
 * @param modifier Permite aplicar modificadores de layout.
 */
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

        // Campo Modelo
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.vehicle,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(vehicle = it)) },
            labelName = { Text("Modelo") },
            modifier = Modifier
        )

        // Campo Placa
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.plate,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(plate = it)) },
            labelName = { Text("Placa") },
            modifier = Modifier
        )

        // Campo Cor
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.color,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(color = it)) },
            labelName = { Text("Cor") },
            modifier = Modifier
        )

        // Campo Ano
        InfoScreenField(
            infoInput = uiState.vehicleInfoDetails.year,
            onInfoInputChange = { onValueChange(uiState.vehicleInfoDetails.copy(year = it)) },
            labelName = { Text("Ano") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

/**
 * Seção de preenchimento dos endereços de origem e destino.
 *
 * @param uiState Estado atual dos dados de endereço.
 * @param onValueChange Callback chamado ao alterar algum campo.
 * @param modifier Permite aplicar modificadores de layout.
 */
@Composable
fun InfoFields(
    uiState: AddressUiState,
    onValueChange: (AddressDetails) -> Unit = {},
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        // Endereço de origem
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

        // Endereço de destino
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

/**
 * Seção de preenchimento do status dos itens do veículo (parte 1).
 * Exibe switches para marcar presença/ausência de acessórios e equipamentos.
 *
 * @param uiState Estado atual dos itens do veículo.
 * @param onValueChange Callback chamado ao alterar algum campo.
 * @param modifier Permite aplicar modificadores de layout.
 */
@Composable
fun VehicleStatusScreen1(
    modifier: Modifier = Modifier,
    uiState: VehicleStatus1UiState,
    onValueChange: (VehicleStatus1Details) -> Unit = {},
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

/**
 * Seção de preenchimento do status dos pneus, nível de combustível e observações.
 *
 * @param modifier Permite aplicar modificadores de layout.
 * @param onValueChange Callback chamado ao alterar algum campo.
 * @param uiState Estado atual dos dados de pneus, combustível e observações.
 */
@Composable
fun VehicleStatusScreen2(
    modifier: Modifier = Modifier,
    onValueChange: (VehicleStatus2Details) -> Unit = {},
    uiState: VehicleStatus2UiState,
) {
    Column(modifier = modifier) {
        // -----------------------------
        // Estado dos pneus dianteiros
        // -----------------------------
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

        // -----------------------------
        // Estado dos pneus traseiros
        // -----------------------------
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

        // -----------------------------
        // Estado do estepe
        // -----------------------------
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

        // -----------------------------
        // Nível de combustível (slider)
        // -----------------------------
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

        // -----------------------------
        // Observações adicionais
        // -----------------------------
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

/**
 * Preview da tela ChecklistScreen com navegação simulada.
 */
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