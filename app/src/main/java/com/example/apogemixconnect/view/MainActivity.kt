package com.example.apogemixconnect.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import com.example.apogemixconnect.model.WebSocketListener
import com.example.apogemixconnect.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val coroutineScope = remember { MainScope() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Arrangement.Center
    ) {
        Input()
        SendCommand()
        ConnectButton()
        DatasReached()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input() : String {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
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
    fun SendCommand(){
        Button(
            shape = RoundedCornerShape(0),
            onClick = {

                webSocket?.send("POLECENIE")
                viewModel.addMessage(Pair(true, "POLECENIE"))
            }) {
            Text(text = "SendCommand")
        }
    }

@Composable
fun DatasReached() {
    Text(text = "Hello !")
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
    ApogemixConnectTheme {
        MainScreen()
    }
}
}