package com.example.apogemixconnect.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.apogemixconnect.ui.theme.ApogemixConnectTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import com.example.apogemixconnect.model.WebSocketListener
import com.example.apogemixconnect.viewmodel.MainViewModel
import kotlinx.coroutines.MainScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp




class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var webSocketListener: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        webSocketListener = WebSocketListener(viewModel)
        setContent {
            ApogemixConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(viewModel: MainViewModel) {
        val coroutineScope = remember { MainScope() }
        val inputText = Input()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            Input()
            SendCommand(inputText)
            Row {
                ConnectButton()
                Spacer(modifier = Modifier.width(1.dp))
                DisconnectButton(viewModel)
            }
            DatasReached(viewModel)
        }
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
    fun ConnectButton(){
        Button(
            shape = RoundedCornerShape(0),
            onClick = {
                webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
        }) {
            Text(text = "Connect")
        }
    }

    @Composable
    fun DisconnectButton(viewModel: MainViewModel){
        Button(
            shape = RoundedCornerShape(0),
            onClick = {
                webSocket?.close(1000, "Canceled manually.")
                viewModel.clearMessage()
            }) {
            Text(text = "Disconnect")
        }
    }



    @Composable
    fun SendCommand(inputText: MutableState<String>) {
        Button(
            shape = RoundedCornerShape(0),
            onClick = {
                webSocket?.send(inputText.value)
                viewModel.addMessage(Pair(true, inputText.value))
            }
        ) {
            Text(text = "SendCommand")
        }
    }


    @Composable
    fun DatasReached(viewModel: MainViewModel) {
        val message by viewModel.messages.observeAsState(Pair(false, ""))
        Text(text = message.second)
    }

    private fun createRequest(): Request {
        val websocketURL = "ws://192.168.4.1/ws"
        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    @Preview(
        showBackground = true,
        showSystemUi = true
    )
    @Composable
    fun Preview() {
        // Create a dummy viewModel for preview purposes
        val dummyViewModel = MainViewModel()

        ApogemixConnectTheme {
            MainScreen(dummyViewModel)
        }
    }

}