package com.example.apogemixconnect.view.Screens.ConnectionScreen

// Android imports
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Jetpack Compose imports
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import com.example.apogemixconnect.model.Data.FlightDB.Flight
import com.example.apogemixconnect.viewmodel.DatabaseViewModel
import com.example.apogemixconnect.viewmodel.WebSocketViewModel

// Styles
import com.example.apogemixconnect.ui.theme.Style.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionScreen(viewModel: WebSocketViewModel, DBviewModel: DatabaseViewModel, navController: NavController, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val flights = DBviewModel.getFlights().collectAsState(initial = emptyList())
            ConnectionStatus(viewModel, navController)
            ConnectionInputs(viewModel)
            NavigateButtons(onClick)
            FlightLazyColumn(onClick, flights.value)
        }
    }
}


@Composable
fun ConnectionStatus(viewModel: WebSocketViewModel, navController: NavController) {
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
            modifier = Modifier.fillMaxHeight()
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
fun ConnectionInputs(viewModel: WebSocketViewModel) {
    val inputText = remember { mutableStateOf("ws://192.168.4.1/ws") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .height(TextFieldHeight)
                .padding(end = 8.dp),
            value = inputText.value,
            onValueChange = { inputText.value = it },
            shape = ButtonShape,
            label = { Text("Your URL") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Button(
            modifier = Modifier
                .height(TextFieldHeight),
            shape = RoundedCornerShape(25),
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
                .weight(1f)
                .height(TextFieldHeight)
                .padding(end = 4.dp),
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = { onClick("ReciverDataScreen") }
        ) {
            Text(text = "Data Receiver")
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .height(TextFieldHeight)
                .padding(start = 4.dp),
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            onClick = { onClick("SendCommandScreen") }
        ) {
            Text(text = "Flight Controller")
        }
    }
}


@Composable
fun FlightLazyColumn(onClick: (String) -> Unit, flights: List<Flight>) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
    ){
        items(items = flights, key = {it.uid}){flight ->
            FlightRow(onClick, flight)
        }

    }
}


@Composable
fun FlightRow(onClick: (String) -> Unit, flight : Flight) {
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(1.dp)
            .clickable{ onClick("DataAnalysis/${flight.uid}/${flight.name}/${flight.date}") },
        shape = RoundedCornerShape(10.dp),

        ){
        Row(modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${flight.uid} ${flight.name}")
            Text(text = "${flight.date}")
        }
    }
}
