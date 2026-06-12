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
import com.example.checklistdigital.ui.screens.AddressInfoScreen
import com.example.checklistdigital.ui.screens.ChecklistHomeScreen
import com.example.checklistdigital.ui.screens.ChecklistScreen
import com.example.checklistdigital.ui.screens.VehicleStatusScreen1
import com.example.checklistdigital.ui.screens.VehicleStatusScreen2



//@Composable
//fun ChecklistNavGraph(
//    navController: NavHostController = rememberNavController()
//){
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = ChecklistMainScreen.valueOf(
//        backStackEntry?.destination?.route?.substringBefore("/") ?: ChecklistMainScreen.Client.name
//    )
//
//    Scaffold(
//        topBar = {
//            ChecklistTopAppBar()
//        }
//    ) { contentPadding ->
//
//        NavHost(
//            navController = navController,
//            startDestination = ChecklistMainScreen.Home.name,
//            modifier = Modifier
//        ){
//            composable(route = ChecklistMainScreen.Home.name){
//                ChecklistHomeScreen(
//                    navigateToItemEntry = { navController.navigate(ChecklistMainScreen.Client.name) },
//                    navigateToItemUpdate = { navController.navigate("${ChecklistMainScreen.Client.name}/$it") },
//                    modifier = Modifier
//                        .padding(contentPadding)
//                )
//            }

            // Route for creating NEW checklist
//            composable(route = ChecklistMainScreen.Client.name) {
//                ClientInfoScreen(
//                    navController = navController,
//                    clientId = -1,
//                    onNextClick = { clientId ->
//                        navController.navigate("${ChecklistMainScreen.Address.name}/$clientId")
//                    },
//                    onBackClick = { navController.navigate(ChecklistMainScreen.Home.name) },
//                    backButtonState = false,
//                    modifier = Modifier
//                        .padding(contentPadding)
//                )
//            }
//
//            // Route for EDITING existing checklist
//            composable(
//                route = "${ChecklistMainScreen.Client.name}/{clientId}",
//                arguments = listOf(navArgument("clientId") { type = NavType.IntType })
//            ) { backStackEntry ->
//                val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
//                ClientInfoScreen(
//                    navController = navController,
//                    clientId = clientId,
//                    onNextClick = { clientId ->
//                        navController.navigate("${ChecklistMainScreen.Address.name}/$clientId")
//                    },
//                    onBackClick = { navController.navigate(ChecklistMainScreen.Home.name) },
//                    backButtonState = true,
//                    modifier = Modifier
//                        .padding(contentPadding)
//                )
//            }
//
//            // Address screen with clientId as route parameter
//            composable(
//                route = "${ChecklistMainScreen.Address.name}/{clientId}",
//                arguments = listOf(navArgument("clientId") { type = NavType.IntType })
//            ) { backStackEntry ->
//                val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
//                AddressInfoScreen(
//                    clientId = clientId,
//                    onNextClick = {
//                        navController.navigate("${ChecklistMainScreen.Vehicle.name}/$clientId")
//                    },
//                    onBackClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .padding(contentPadding)
//                )
//            }
//
//            // Vehicle Status 1 screen with clientId as route parameter
//            composable(
//                route = "${ChecklistMainScreen.Vehicle.name}/{clientId}",
//                arguments = listOf(navArgument("clientId") { type = NavType.IntType })
//            ) { backStackEntry ->
//                val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
//                VehicleStatusScreen1(
//                    clientId = clientId,
//                    onNextClick = {
//                        navController.navigate("${ChecklistMainScreen.Info.name}/$clientId")
//                    },
//                    onBackClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .padding(contentPadding)
//                )
//            }
//
//            // Vehicle Status 2 screen with clientId as route parameter
//            composable(
//                route = "${ChecklistMainScreen.Info.name}/{clientId}",
//                arguments = listOf(navArgument("clientId") { type = NavType.IntType })
//            ) { backStackEntry ->
//                val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1
//                VehicleStatusScreen2(
//                    clientId = clientId,
//                    onNextClick = { navController.navigate(ChecklistMainScreen.Home.name) },
//                    onBackClick = { navController.popBackStack() },
//                    modifier = Modifier
//                        .padding(contentPadding)
//                )
//            }
//        }
//    }
//}

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
                    contentDescription = null
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

enum class ChecklistMainScreen() {
    Home,
    Client,
    Address,
    Vehicle,
    Info,
}

//@Composable
//@Preview
//fun ChecklistMainScreenPreview(){
//    ChecklistNavGraph()
//}
