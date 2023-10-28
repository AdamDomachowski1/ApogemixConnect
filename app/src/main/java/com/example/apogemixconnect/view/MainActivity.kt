package com.example.apogemixconnect.view

// Android imports
import android.nfc.Tag
import android.os.Bundle
import android.util.Log

// Jetpack imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

// Compose UI imports
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Compose Foundation imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

// Compose Material3 imports
import androidx.compose.material3.*

// Compose Runtime imports
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.apogemixconnect.Data.FlightDatas

// Project-specific imports
import com.example.apogemixconnect.ui.theme.ApogemixConnectTheme
import com.example.apogemixconnect.model.WebSocketListener
import com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen
import com.example.apogemixconnect.ui.theme.Screens.SettingsScreen
import com.example.apogemixconnect.viewmodel.MainViewModel

// Other imports
import kotlinx.coroutines.MainScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


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
                    composable("settings") {
                        SettingsScreen(onClick = {
                            navController.navigate(
                                route = it,
                                navOptions = navOptions {
                                    popUpTo("ConnectionScreen") { inclusive = true }
                                })
                        })
                    }
                }
            }
        }

//        @Composable
//        fun MainScreen(viewModel: MainViewModel) {
//            val coroutineScope = remember { MainScope() }
//            val inputText = Input()
//            val urlText = remember { mutableStateOf("ws://192.168.4.1/ws") }  // Default URL value
//
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Top,
//            ) {
//                InputURL(urlText) // Nowy komponent do wprowadzania adresu URL
//                Input()
//                SendCommand(inputText)
//                Row {
//                    ConnectButton(urlText.value) // Przesłanie wprowadzonego adresu URL
//                    Spacer(modifier = Modifier.width(1.dp))
//                    DisconnectButton()
//                }
//                DisplayFlightData(viewModel)
//            }
//        }

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

//        @Composable
//        fun ConnectButton(url: String) {
//            Button(
//                shape = RoundedCornerShape(0),
//                onClick = {
//                    viewModel.connect(url)
//                }) {
//                Text(text = "Connect")
//            }
//        }
//
//        @Composable
//        fun DisconnectButton() {
//            Button(
//                shape = RoundedCornerShape(0),
//                onClick = {
//                    viewModel.disconnect()
//                }) {
//                Text(text = "Disconnect")
//            }
//        }

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


//    @Composable
//    fun RawDatasReached(viewModel: MainViewModel) {
//        val message by viewModel.messages.observeAsState(Pair(false, ""))
//        Text(text = message.second)
//    }

        @Composable
        fun DisplayFlightData(viewModel: MainViewModel) {
            val flightData by viewModel.flightData.observeAsState(
                FlightDatas(
                    "",
                    0f,
                    0f,
                    0f,
                    0,
                    0f,
                    0f,
                    0f,
                    0f,
                    0,
                    0
                )
            )
            Column() {
                Text(text = "Name: ${flightData.name}")
                Text(text = "GPS_Lat: ${flightData.gpsLat}")
                Text(text = "GPS_Lng: ${flightData.gpsLng}")
                Text(text = "GPS_Alt: ${flightData.gpsAlt}")
                Text(text = "Time: ${flightData.time}")
                Text(text = "Temperature: ${flightData.temperature}")
                Text(text = "Pressure: ${flightData.pressure}")
                Text(text = "Height: ${flightData.height}")
                Text(text = "Speed: ${flightData.speed}")
                Text(text = "Continuity: ${flightData.continuity}")
                Text(text = "State: ${flightData.state}")
            }
        }


        private fun createRequest(adress: String): Request {
            //val websocketURL = "ws://192.168.4.1/ws"
            return Request.Builder()
                .url(adress)
                .build()
        }
    }
