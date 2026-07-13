package com.example.checklistdigital.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PhotoScreen(
    clientId: Int = -1,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()){

        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = "PhotoScreen of client nº ${clientId}")
            InfoScreenButtons(
                text1 = "Cancelar",
                onBackClick = onBackClick,
                text2 = "Salvar",
                nextButtonState = false,
                onNextClick = {},
                modifier = Modifier
            )
        }
    }

}