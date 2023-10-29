package com.example.apogemixconnect.ui.theme.Screens.SendCommandScreen

// AndroidX and Compose imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionButtons
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen.DisplayFlightData

// External libraries imports

// Project specific imports
import com.example.apogemixconnect.viewmodel.MainViewModel

@Composable
fun SendCommandScreen(viewModel: MainViewModel, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectionStatus(viewModel)
        CommandsButtons(viewModel = viewModel)
        DisplayFlightData(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(defaultValue: String = "Test Message CODE 000XF"): MutableState<String> {
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
fun CommandsButtons(viewModel: MainViewModel) {
    val inputText = InputField()
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
                viewModel.sendCommand(inputText.value)  // Using input value
            }
        ) {
            Text(text = "Send")
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