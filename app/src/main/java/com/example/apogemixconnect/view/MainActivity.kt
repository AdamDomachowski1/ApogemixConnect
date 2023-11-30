package com.example.apogemixconnect.view

// Android import
import android.os.Bundle

// Jetpack imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Compose Runtime imports
import com.example.apogemixconnect.view.Screens.DataAnalysis.DataAnalysis
import com.example.apogemixconnect.view.Screens.MainPage.MainPage
import com.example.apogemixconnect.view.Screens.ConnectionScreen.ConnectionScreen
import com.example.apogemixconnect.view.Screens.ReciverDataScreen.ReciverDataScreen
import com.example.apogemixconnect.view.Screens.SendCommandScreen.SendCommandScreen

// Project-specific imports
import com.example.apogemixconnect.viewmodel.DatabaseViewModel
import com.example.apogemixconnect.viewmodel.WebSocketViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: WebSocketViewModel
    private lateinit var DBviewModel: DatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebSocketViewModel::class.java)
        DBviewModel = ViewModelProvider(this).get(DatabaseViewModel::class.java)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "MainPage") {
                // Definicje tras nawigacyjnych
                composable("MainPage") {
                    MainPage(viewModel, navController, onClick = {
                        navController.navigate(it)
                    })
                }

                composable("dataAnalysis/{uid}",
                    arguments = listOf(navArgument("uid") { type = NavType.IntType })
                ) { backStackEntry ->
                    val uid = backStackEntry.arguments?.getInt("uid") ?: -1
                    DataAnalysis(viewModel, DBviewModel, navController, uid)
                }

                composable("ConnectionScreen") {
                    ConnectionScreen(viewModel, DBviewModel, navController, onClick = {
                        navController.navigate(it)
                    })
                }

                composable("ReciverDataScreen") {
                    ReciverDataScreen(viewModel, DBviewModel, navController, onClick = {
                        navController.navigate(it)
                    })
                }

                composable("SendCommandScreen") {
                    SendCommandScreen(viewModel, navController, onClick = {
                        navController.navigate(it)
                    })
                }
            }
        }
    }
}
