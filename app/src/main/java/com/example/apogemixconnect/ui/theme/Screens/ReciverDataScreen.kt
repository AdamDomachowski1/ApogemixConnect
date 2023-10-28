package com.example.apogemixconnect.ui.theme.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apogemixconnect.Data.FlightDatas
import com.example.apogemixconnect.viewmodel.MainViewModel

@Composable
fun ReciverDataScreen(viewModel: MainViewModel, onClick: (String) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "ReciverDataScreen")
        DisplayFlightData(viewModel)
    }

}

@Composable
fun DisplayFlightData(viewModel: MainViewModel) {
    val flightData by viewModel.flightData.observeAsState(
        FlightDatas("", 0f, 0f, 0f, 0, 0f, 0f, 0f, 0f, 0, 0)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
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