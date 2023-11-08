package com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen

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
import com.example.apogemixconnect.viewmodel.MainViewModel

// Constants for UI Design
private val BoxHeight = 50.dp
private val TextFieldHeight = 60.dp
private val ButtonHeight = 50.dp
private val HorizontalPadding = 8.dp
private val ButtonShape = RoundedCornerShape(25)
private val ArrowIconSize = 40.dp
private val SpacerWidth = 40.dp
val ButtonColor = Color(0xFF6A205E)
val BackgroundColor = Color(0xFF00072e)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionScreen(viewModel: MainViewModel, navController: NavController, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor) // Apply the background color here
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConnectionStatus(viewModel, navController)
            ConnectionInputs(viewModel)
            NavigateButtons(onClick)
        }
    }
}


@Composable
fun ConnectionStatus(viewModel: MainViewModel, navController: NavController) {
    val isConnected by viewModel.socketStatus.observeAsState(initial = false)
    val statusText = if (isConnected) "Connected" else "No connection"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BoxHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Go back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(ArrowIconSize),
                tint = Color(0xFF82C2F0)
            )
            Text(
                text = statusText,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = HorizontalPadding)
            )
            Spacer(modifier = Modifier.width(SpacerWidth))
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ConnectionInputs(viewModel: MainViewModel) {
    val inputText = remember { mutableStateOf("ws://192.168.4.1/ws") }

    Row(
        verticalAlignment = Alignment.CenterVertically, // To align the button vertically with the TextField
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // Add padding around the Row for visual spacing
    ) {
        TextField(
            modifier = Modifier
                .weight(1f) // TextField will take up the majority of the space
                .height(TextFieldHeight)
                .padding(end = 8.dp), // Add padding at the end of TextField for spacing between the TextField and the Button
            value = inputText.value,
            onValueChange = { inputText.value = it },
            shape = ButtonShape,
            label = { Text("Your URL") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent, // Hide underline when not focused
            focusedIndicatorColor = Color.Transparent // Hide underline when focused
        )
        )

        Button(
            modifier = Modifier
                .height(TextFieldHeight), // Match the height with TextField for alignment
            shape = RoundedCornerShape(25), // Apply a large corner radius for a rounded rectangle shape
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = { viewModel.connect(inputText.value) }
        ) {
            Text(text = "Connect")
        }
    }
}



@Composable
fun NavigateButtons(onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            modifier = Modifier
                .weight(1f) // Each button will take half the width
                .height(TextFieldHeight)
                .padding(end = 4.dp), // Add padding to separate the buttons
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = { onClick("ReciverDataScreen") }
        ) {
            Text(text = "Data Receiver")
        }
        Button(
            modifier = Modifier
                .weight(1f) // Each button will take half the width
                .height(TextFieldHeight)
                .padding(start = 4.dp), // Add padding to separate the buttons
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = { onClick("SendCommandScreen") }
        ) {
            Text(text = "Flight Controller")
        }
    }
}
