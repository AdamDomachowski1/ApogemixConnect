package com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen

// AndroidX and Compose imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// External libraries imports

// Project specific imports
import com.example.apogemixconnect.viewmodel.MainViewModel

@Composable
fun ConnectionScreen(viewModel: MainViewModel, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectionStatus(viewModel)
        ConnectionInputs(viewModel)
        NavigateButtons(onClick)
    }
}

@Composable
fun ConnectionStatus(viewModel: MainViewModel) {
    // Obserwuj socketStatus za pomocą observeAsState()
    val isConnected = viewModel.socketStatus.observeAsState(initial = false).value

    val text = if (isConnected) "connected" else "no connection"
    Text(text = text)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionInputs(viewModel: MainViewModel) {
    val inputText = InputField()

    ConnectionButtons(viewModel, inputText)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(defaultValue: String = "ws://192.168.4.1/ws"): MutableState<String> {
    val text =  remember { mutableStateOf(defaultValue) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 1.dp),
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("Your URL") }
    )
    return text
}

@Composable
fun ConnectionButtons(viewModel: MainViewModel, inputText: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .padding(end = 1.dp),
            shape = RoundedCornerShape(0),
            onClick = {
                viewModel.connect(inputText.value)  // Using input value
            }
        ) {
            Text(text = "Connect")
        }

        Button(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .padding(start = 1.dp),
            shape = RoundedCornerShape(0),
            onClick = {
                viewModel.disconnect()
            }
        ) {
            Text(text = "Disconnect")
        }
    }
}

@Composable
fun NavigateButtons(onClick: (String) -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(bottom = 1.dp),
        shape = RoundedCornerShape(0),
        onClick = {
            onClick("ReciverDataScreen")
        }
    ) {
        Text(text = "ReciverDataScreen")
    }

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(bottom = 1.dp),
        shape = RoundedCornerShape(0),
        onClick = {
            onClick("SendCommandScreen")
        }
    ) {
        Text(text = "SendCommandScreen")
    }
}