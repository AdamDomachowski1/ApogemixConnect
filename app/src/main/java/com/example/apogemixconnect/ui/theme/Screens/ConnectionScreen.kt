package com.example.apogemixconnect.ui.theme.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apogemixconnect.Data.FlightDatas
import com.example.apogemixconnect.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@Composable
fun ConnectionScreen(viewModel: MainViewModel, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "ConnectionScreen")
        Input()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 1.dp),  // dodajemy padding na dole
        ) {
            Button(
                modifier = Modifier
                    .weight(1f) // dzieli dostępną szerokość na pół
                    .height(50.dp)
                    .padding(end = 1.dp), // dodaje odstęp między przyciskami
                shape = RoundedCornerShape(0),
                onClick = {
                    viewModel.connect("ws://192.168.4.1/ws")
                }
            ) {
                Text(text = "Connect")
            }

            Button(
                modifier = Modifier
                    .weight(1f) // dzieli dostępną szerokość na pół
                    .height(50.dp)
                    .padding(start = 1.dp), // dodaje odstęp między przyciskami
                shape = RoundedCornerShape(0),
                onClick = {
                    viewModel.disconnect()
                }
            ) {
                Text(text = "Disconnect")
            }
        }



        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 1.dp),
            shape = RoundedCornerShape(0),
            onClick = {
            onClick("ReciverDataScreen")
        }) {
            Text(text = "ReciverDataScreen")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 1.dp),
            shape = RoundedCornerShape(0),
            onClick = {
            onClick("settings")
        }) {
            Text(text = "Settings")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(): MutableState<String> {
    val text = remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(bottom = 1.dp),
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("Yours URL") }
    )
    return text
}

private fun createRequest(adress: String): Request {
    return Request.Builder()
        .url(adress)
        .build()
}

