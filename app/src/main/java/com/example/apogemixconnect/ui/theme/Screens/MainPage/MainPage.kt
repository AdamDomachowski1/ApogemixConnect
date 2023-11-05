package com.example.apogemixconnect.ui.theme.Screens.MainPage

// AndroidX and Compose imports
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// External libraries imports

// Project specific imports
import com.example.apogemixconnect.viewmodel.MainViewModel

@Composable
fun ConnectionScreen(viewModel: MainViewModel, navController: NavController, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectionStatus(viewModel, navController)
        ConnectionInputs(viewModel)
        NavigateButtons(onClick)
    }
}

@Composable
fun ConnectionStatus(viewModel: MainViewModel, navController: NavController) {
    // Obserwuj socketStatus za pomocą observeAsState()
    val isConnected = viewModel.socketStatus.observeAsState(initial = false).value

    val text = if (isConnected) "Connected" else "No connection"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black) // Ustaw tło Box na czarne
            .height(50.dp),
        contentAlignment = Alignment.CenterStart, // Wyśrodkowanie zawartości Box po lewej stronie
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize() // fill the height of the Box
                .padding(start = 16.dp) // You can adjust the padding as needed
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Go back",
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
                    .size(40.dp)
                    .align(Alignment.CenterVertically),
                tint = Color(0xFF82C2F0)
            )
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .align(Alignment.CenterVertically) // align the text to the center vertically
            )
            Spacer(
                modifier = Modifier.width(40.dp)
            )
        }
    }
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