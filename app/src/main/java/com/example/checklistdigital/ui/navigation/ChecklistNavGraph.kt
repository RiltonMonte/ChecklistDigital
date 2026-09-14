package com.example.checklistdigital.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.checklistdigital.R
import com.example.checklistdigital.ui.screens.ChecklistHomeScreen
import com.example.checklistdigital.ui.screens.ChecklistScreen
import com.example.checklistdigital.ui.screens.PhotoScreen



/**
 * Função responsável por configurar e gerenciar a navegação principal do aplicativo.
 * Define as rotas e telas disponíveis dentro do fluxo de checklist.
 *
 * @param modifier Permite aplicar modificadores de layout.
 * @param navController Controlador de navegação usado para gerenciar as rotas.
 */
@Composable
fun ChecklistNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold{ contentPadding ->

        NavHost(
            navController = navController,
            startDestination = ChecklistMainScreen.Home.name, // Tela inicial definida como "Home"
            modifier = modifier
        ){
            // Rota para a tela inicial (lista de checklists)
            composable(route = ChecklistMainScreen.Home.name){
                ChecklistHomeScreen(
                    navigateToItemEntry = { navController.navigate(ChecklistMainScreen.Checklist.name) },
                    navigateToItemUpdate = { navController.navigate("${ChecklistMainScreen.Checklist.name}/$it") },
                    navigateToPhoto = { navController.navigate("${ChecklistMainScreen.Photo.name}/$it") },
                    modifier = Modifier
                        .padding(contentPadding)
                )
            }
            // Rota para criação de um NOVO checklist
            composable(route = ChecklistMainScreen.Checklist.name) {
                ChecklistScreen(
                    navController = navController,
                    clientId = -1, // -1 indica criação de novo checklist
                    onNextClick = { navController.navigate(ChecklistMainScreen.Home.name) },
                    onBackClick = { navController.navigate(ChecklistMainScreen.Home.name) },
                    modifier = Modifier
                        .padding(contentPadding)
                )
            }
            // Rota para EDIÇÃO de checklist existente
            composable(
                route = "${ChecklistMainScreen.Checklist.name}/{clientId}",
                arguments = listOf(navArgument("clientId") { type = NavType.IntType })
            ) { backStackEntry ->
                val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
                ChecklistScreen(
                    navController = navController,
                    clientId = clientId,
                    onNextClick = { navController.navigate(ChecklistMainScreen.Home.name) },
                    onBackClick = { navController.navigate(ChecklistMainScreen.Home.name) },
                    modifier = Modifier
                        .padding(contentPadding)
                )
            }
            // Rota para tela de fotos vinculadas ao checklist
            composable(
                route = "${ChecklistMainScreen.Photo.name}/{clientId}",
                arguments = listOf(navArgument("clientId") { type = NavType.IntType })
            ){ backStackEntry ->
                val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
                PhotoScreen(
                    clientId = clientId,
                    onBackClick = { navController.navigate(ChecklistMainScreen.Home.name) },
                    modifier = Modifier
                        .padding(contentPadding)
                )

            }
        }
    }
}

/**
 * Barra superior personalizada do aplicativo.
 * Exibe o logotipo e o nome do app centralizados.
 *
 * @param modifier Permite aplicar modificadores de layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistTopAppBar(modifier: Modifier = Modifier){
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp),
                    painter = painterResource(R.drawable.logo_apg),
                    contentDescription = null // Imagem decorativa, sem descrição
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        modifier = modifier
    )

}

/**
 * Enum que define as telas principais do fluxo de checklist.
 * Utilizado para facilitar a navegação entre rotas.
 */
enum class ChecklistMainScreen() {
    Home,
    Checklist,
    Photo
}

/**
 * Função de preview para visualização da navegação principal
 * dentro do Android Studio sem necessidade de execução completa.
 */
@Composable
@Preview
fun ChecklistMainScreenPreview(){
    ChecklistNavGraph()
}
