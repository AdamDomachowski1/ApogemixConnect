package com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen

// AndroidX and Compose imports
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Project specific imports
import com.example.apogemixconnect.Data.FlightDatas
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.viewmodel.MainViewModel

@Composable
fun ReciverDataScreen(viewModel: MainViewModel, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectionStatus(viewModel)
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
            .padding(16.dp)
    ) {
        FlightDataRow(label = "Name", value = flightData.name)
        FlightDataRow(label = "GPS_Lat", value = flightData.gpsLat.toString())
        FlightDataRow(label = "GPS_Lng", value = flightData.gpsLng.toString())
        FlightDataRow(label = "GPS_Alt", value = flightData.gpsAlt.toString())
        FlightDataRow(label = "Time", value = flightData.time.toString())
        FlightDataRow(label = "Temperature", value = flightData.temperature.toString())
        FlightDataRow(label = "Pressure", value = flightData.pressure.toString())
        FlightDataRow(label = "Height", value = flightData.height.toString())
        FlightDataRow(label = "Speed", value = flightData.speed.toString())
        FlightDataRow(label = "Continuity", value = flightData.continuity.toString())
        FlightDataRow(label = "State", value = flightData.state.toString())
    }
}

@Composable
fun FlightDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Text(text = value, modifier = Modifier.weight(1f))
    }
}
