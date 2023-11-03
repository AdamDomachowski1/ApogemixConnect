package com.example.apogemixconnect.view

// Android imports
import android.os.Bundle

// Jetpack imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

// Compose UI imports

// Compose Foundation imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

// Compose Material3 imports
import androidx.compose.material3.*

// Compose Runtime imports
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Project-specific imports
import com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen.ReciverDataScreen
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionScreen
import com.example.apogemixconnect.ui.theme.Screens.SendCommandScreen.SendCommandScreen
import com.example.apogemixconnect.viewmodel.MainViewModel

// Other imports
import okhttp3.Request


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

            setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "ConnectionScreen") {
                    composable("ConnectionScreen") {
                        ConnectionScreen(viewModel, onClick = {
                            navController.navigate(it)
                        })
                    }
                    composable("ReciverDataScreen") {
                        ReciverDataScreen(viewModel, onClick = {
                            navController.navigate(it)
                        })
                    }
                    composable("SendCommandScreen") {
                        SendCommandScreen(viewModel, onClick = {
                        navController.navigate(it)
                        })
                    }
                }
            }
        }


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun InputURL(url: MutableState<String>) {
            TextField(
                value = url.value,
                onValueChange = { url.value = it },
                label = { Text("Enter WebSocket URL") }
            )
        }


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Input(): MutableState<String> {
            val text = remember { mutableStateOf("") }

            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Send Command") }
            )
            return text
        }


        @Composable
        fun SendCommand(inputText: MutableState<String>) {
            Button(
                shape = RoundedCornerShape(0),
                onClick = {
                    viewModel.sendCommand(inputText.value)
                }
            ) {
                Text(text = "SendCommand")
            }
        }


        private fun createRequest(adress: String): Request {
            //val websocketURL = "ws://192.168.4.1/ws"
            return Request.Builder()
                .url(adress)
                .build()
        }
    }
