package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Campo de entrada de texto reutilizável para telas de informação.
 *
 * @param modifier Permite aplicar modificadores de layout.
 * @param infoInput Valor atual do campo de texto.
 * @param onInfoInputChange Callback chamado quando o valor do campo muda.
 * @param labelName Composable que define o rótulo do campo.
 */
@Composable
fun InfoScreenField(
    modifier: Modifier = Modifier,
    infoInput: String,
    onInfoInputChange: (String) -> Unit,
    labelName: @Composable () -> Unit
){
    Column(modifier = modifier) {
        OutlinedTextField(
            value = infoInput,
            singleLine = true,
            onValueChange = onInfoInputChange,
            label = labelName,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        )
    }
}

/**
 * Conjunto de botões "Voltar" e "Próximo" para navegação entre telas.
 *
 * @param onBackClick Ação ao clicar em "Voltar".
 * @param onNextClick Ação ao clicar em "Próximo".
 * @param backButtonState Define se o botão "Voltar" está habilitado.
 * @param nextButtonState Define se o botão "Próximo" está habilitado.
 * @param text1 Texto exibido no botão "Voltar".
 * @param text2 Texto exibido no botão "Próximo".
 * @param modifier Permite aplicar modificadores de layout.
 */
@Composable
fun InfoScreenButtons(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    backButtonState: Boolean = true,
    nextButtonState: Boolean = true,
    text1: String,
    text2: String,
){
    Row(
        modifier = modifier.padding(8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = onBackClick,
            enabled = backButtonState,
            modifier = Modifier
        ) {
            Text(text = text1)
        }
        Button(
            onClick = onNextClick,
            enabled = nextButtonState,
            modifier = Modifier
        ) {
            Text(text = text2)
        }
    }
}

/**
 * Linha contendo dois switches (interruptores) para seleção de opções booleanas.
 *
 * @param info1 Texto descritivo do primeiro switch.
 * @param infoChecked1 Estado atual do primeiro switch.
 * @param onInfoChecked1Change Callback chamado ao alterar o estado do primeiro switch.
 * @param info2 Texto descritivo do segundo switch.
 * @param infoChecked2 Estado atual do segundo switch.
 * @param onInfoChecked2Change Callback chamado ao alterar o estado do segundo switch.
 * @param modifier Permite aplicar modificadores de layout.
 */
@Composable
fun SwitchRow(
    modifier: Modifier = Modifier,
    info1: String,
    infoChecked1: Boolean = false,
    onInfoChecked1Change: (Boolean) -> Unit = {},
    info2: String,
    infoChecked2: Boolean = false,
    onInfoChecked2Change: (Boolean) -> Unit = {},

){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Card(modifier = Modifier.padding(2.dp).fillMaxWidth(.5f)) {
            Column(modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = info1,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                )
                Switch(
                    checked = infoChecked1,
                    onCheckedChange = onInfoChecked1Change,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
        }

        Card(modifier = Modifier.padding(2.dp).fillMaxWidth(1f)) {
            Column(modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = info2,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                )
                Switch(
                    checked = infoChecked2,
                    onCheckedChange = onInfoChecked2Change,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
        }
    }
}

/**
 * Conjunto de botões de seleção (RadioButtons) para escolha de uma opção.
 *
 * @param radioOptions Lista de opções disponíveis.
 * @param vehicleDetails2 Índice da opção atualmente selecionada.
 * @param onClickChange Callback chamado ao selecionar uma nova opção (retorna índice).
 * @param onOptionSelected Callback chamado ao selecionar uma nova opção (retorna texto).
 * @param modifier Permite aplicar modificadores de layout.
 */
@Composable
fun ButtonSelection(
    modifier: Modifier = Modifier,
    radioOptions: List<String>,
    vehicleDetails2: Int,
    onClickChange: (Int) -> Unit = {},
    onOptionSelected: (String) -> Unit = {},
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

/**
 * Previews para facilitar a visualização dos componentes
 * diretamente no Android Studio sem necessidade de execução.
 */
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun InfoScreenFieldPreview(){
    InfoScreenField(
        infoInput = "",
        onInfoInputChange = {},
        labelName = { Text("Nome") },
        modifier = Modifier
    )
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun InfoScreenButtonsPreview(){
    InfoScreenButtons(
        text1 = "Voltar",
        text2 = "Próximo",
        modifier = Modifier
    )
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun SwitchRowPreview(){
    SwitchRow(
        info1 = "Item 1",
        infoChecked1 = false,
        onInfoChecked1Change = {},
        info2 = "Item 2",
        infoChecked2 = false,
        onInfoChecked2Change = {}
    )
}

