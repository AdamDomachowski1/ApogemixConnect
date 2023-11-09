package com.example.apogemixconnect.view

// Android imports
import android.os.Bundle

// Jetpack imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

// Compose UI imports

// Compose Foundation imports

// Compose Material3 imports

// Compose Runtime imports
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen.ReciverDataScreen
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionScreen
import com.example.apogemixconnect.ui.theme.Screens.MainPage.MainPage
import com.example.apogemixconnect.ui.theme.Screens.SendCommandScreen.SendCommandScreen
import com.example.apogemixconnect.viewmodel.DatabaseViewModel

// Project-specific imports
import com.example.apogemixconnect.viewmodel.MainViewModel

// Other imports


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var DBviewModel: DatabaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            DBviewModel = ViewModelProvider(this).get(DatabaseViewModel::class.java)

            setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "MainPage") {

                    composable("MainPage") {
                        MainPage(viewModel, navController, onClick = {
                            navController.navigate(it)
                        })
                    }

                    composable("ConnectionScreen") {
                        ConnectionScreen(viewModel, DBviewModel, navController, onClick = {
                            navController.navigate(it)
                        })
                    }

                    composable("ReciverDataScreen") {
                        ReciverDataScreen(viewModel, navController, onClick = {
                            navController.navigate(it)
                        })
                    }

                    composable("SendCommandScreen") {
                        SendCommandScreen(viewModel,navController, onClick = {
                            navController.navigate(it)
                        })
                    }

                }
            }
        }
    }
