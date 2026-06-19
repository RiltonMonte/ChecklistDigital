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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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

@Composable
fun InfoScreenButtons(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    backButtonState: Boolean = true,
    nextButtonState: Boolean = true,
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
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

