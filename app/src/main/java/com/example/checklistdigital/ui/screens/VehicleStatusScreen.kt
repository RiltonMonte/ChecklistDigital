package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.checklistdigital.viewmodel.ChecklistViewModelProvider
import com.example.checklistdigital.viewmodel.VehicleStatus1ViewModel
import com.example.checklistdigital.viewmodel.VehicleStatus2Details
import com.example.checklistdigital.viewmodel.VehicleStatus2ViewModel
import kotlinx.coroutines.launch

@Composable
fun VehicleStatusScreen1(
    onNextClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier,
    vehicleStatus1ViewModel: VehicleStatus1ViewModel = viewModel(factory = ChecklistViewModelProvider.Factory),
){
    val vehicleDetails1 = vehicleStatus1ViewModel.vehicleStatus1UiState.vehicleStatus1Details
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        SwitchRow(
            info1 = "Documentos",
            infoChecked1 = vehicleDetails1.documentos,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(documentos = it))},
            info2 = "Extintor",
            infoChecked2 = vehicleDetails1.extintor,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(extintor = it))}
        )
        SwitchRow(
            info1 = "Livreto",
            infoChecked1 = vehicleDetails1.livreto,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(livreto = it))},
            info2 = "Tapetes",
            infoChecked2 = vehicleDetails1.tapetes,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(tapetes = it))}
        )
        SwitchRow(
            info1 = "Radio",
            infoChecked1 = vehicleDetails1.radio,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(radio = it))},
            info2 = "Estepe",
            infoChecked2 = vehicleDetails1.estepe,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(estepe = it))}
        )
        SwitchRow(
            info1 = "CD Player",
            infoChecked1 = vehicleDetails1.cdPlayer,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(cdPlayer = it))},
            info2 = "Acendedor de Cigarro",
            infoChecked2 = vehicleDetails1.acededorDeCigarro,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(acededorDeCigarro = it))}
        )
        SwitchRow(
            info1 = "DVD Player",
            infoChecked1 = vehicleDetails1.dvdPlayer,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(dvdPlayer = it))},
            info2 = "Macaco",
            infoChecked2 = vehicleDetails1.macaco,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(macaco = it))}
        )
        SwitchRow(
            info1 = "Modulo/Amplificador",
            infoChecked1 = vehicleDetails1.moduloAmplificador,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(moduloAmplificador = it))},
            info2 = "Chave de Roda",
            infoChecked2 = vehicleDetails1.chaveDeRoda,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(chaveDeRoda = it))}
        )
        SwitchRow(
            info1 = "Frente CD",
            infoChecked1 = vehicleDetails1.frenteCD,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(frenteCD = it))},
            info2 = "Triangulo",
            infoChecked2 = vehicleDetails1.triangulo,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(triangulo = it))}
        )
        SwitchRow(
            info1 = "Antena",
            infoChecked1 = vehicleDetails1.antena,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(antena = it))},
            info2 = "Bateria",
            infoChecked2 = vehicleDetails1.bateria,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(bateria = it))}
        )
        SwitchRow(
            info1 = "Roda Liga Leve",
            infoChecked1 = vehicleDetails1.rodaLigaLeve,
            onInfoChecked1Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(rodaLigaLeve = it))},
            info2 = "Pint. Suja Dif. Vistoria",
            infoChecked2 = vehicleDetails1.pintSujaDif,
            onInfoChecked2Change = { vehicleStatus1ViewModel.updateUiState(vehicleDetails1.copy(pintSujaDif = it))}
        )
        Spacer(modifier = Modifier.weight(1f))
        InfoScreenButtons(
            onNextClick = {
                coroutineScope.launch {
                    vehicleStatus1ViewModel.saveVehicleStatus1()
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
fun VehicleStatusScreen2(
    onNextClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier,
    vehicleStatus2ViewModel: VehicleStatus2ViewModel = viewModel(factory = ChecklistViewModelProvider.Factory)
){
    val vehicleDetails2 = vehicleStatus2ViewModel.vehicleStatus2UiState.vehicleStatus2Details
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(top = 8.dp)){
            Text(
                text = "Pneus Dianteiros",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(.30f)
            )
            Card(modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)) {
                ButtonSelection(
                    radioOptions = listOf("Novos", "Bons", "Ruins"),
                    vehicleDetails2 = vehicleDetails2.pneusDianteiros,
                    onClickChange = { vehicleStatus2ViewModel.updateUiState(vehicleDetails2.copy(pneusDianteiros = it)) },
                    modifier = Modifier
                )
            }

        }
        Row (modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Pneus Traseiros",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(.30f)
            )
            Card(modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)){
                ButtonSelection(
                    radioOptions = listOf("Novos", "Bons", "Ruins"),
                    vehicleDetails2 = vehicleDetails2.pneusTraseiros,
                    onClickChange = { vehicleStatus2ViewModel.updateUiState(vehicleDetails2.copy(pneusTraseiros = it)) },
                    modifier = Modifier
                )
            }

        }
        Row (modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Estepe",
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(.30f)
            )
            Card(modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)){
                ButtonSelection(
                    radioOptions = listOf("Novos", "Bons", "Ruins"),
                    vehicleDetails2 = vehicleDetails2.estepe,
                    onClickChange = { vehicleStatus2ViewModel.updateUiState(vehicleDetails2.copy(estepe = it)) },
                    modifier = Modifier
                )
            }
        }
        //marcador de nivel de combustivel
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 24.dp)
        ) {
            Text(text = "Nivel de Combustível: ${(vehicleDetails2.nivelCombustivel*100).toInt()} %")
            Slider(value = vehicleDetails2.nivelCombustivel, onValueChange = { vehicleStatus2ViewModel.updateUiState(vehicleDetails2.copy(nivelCombustivel = it)) })
        }
        //campo de texto para Observações
        Column(modifier = Modifier) {
            OutlinedTextField(
                value = vehicleDetails2.observacoes,
                singleLine = false,
                onValueChange = { vehicleStatus2ViewModel.updateUiState(vehicleDetails2.copy(observacoes = it)) },
                minLines = 5,
                maxLines = 15,
                label = { Text("Observações") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        InfoScreenButtons(
            onNextClick = {
                coroutineScope.launch {
                    vehicleStatus2ViewModel.saveVehicleStatus2()
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
fun ButtonSelection(
    radioOptions: List<String>,
    vehicleDetails2: Int,
    onClickChange: (Int) -> Unit = {},
    onOptionSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
){
    Row(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Column (
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == radioOptions[vehicleDetails2]),
                        onClick = {onClickChange(radioOptions.indexOf(text))},
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
                RadioButton(
                    selected = (text == radioOptions[vehicleDetails2]),
                    onClick = null
                )
            }
        }
    }
}

//@Composable
//fun ButtonSelection(
//    vehicleDetails2: Int,
//    onClickChange: (VehicleStatus2Details) -> Unit = {},
//    modifier: Modifier = Modifier
//){
//    val radioOptions = listOf("Novos", "Bons", "Ruins")
//    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
//    Row(modifier.selectableGroup()) {
//        radioOptions.forEach { text ->
//            Column (
//                Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//                    .selectable(
//                        selected = (text == selectedOption),
//                        onClick = { onOptionSelected(text) },
//                        role = Role.RadioButton
//                    )
//                    .padding(horizontal = 16.dp)
//                    .weight(1f),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = text,
//                    fontSize = 14.sp,
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(start = 16.dp)
//                )
//                RadioButton(
//                    selected = (text == selectedOption),
//                    onClick = null
//                )
//            }
//        }
//    }
//}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun VehicleStatusScreen1Preview(){
    VehicleStatusScreen1(modifier = Modifier)
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun VehicleStatusScreen2Preview(){
    VehicleStatusScreen2(modifier = Modifier)
}

