import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.apogemixconnect.viewmodel.DatabaseViewModel
import com.example.apogemixconnect.viewmodel.WebSocketViewModel
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas
import kotlinx.coroutines.flow.Flow

// Dodaj dodatkowe importy, jeśli są potrzebne

@Composable
fun DataAnalysis(
    viewModel: WebSocketViewModel,
    DBviewModel: DatabaseViewModel,
    navController: NavController,
    param: String
) {
    val flightDatas by DBviewModel.repo.getFlightDatasByUid(param.toInt()).collectAsState(initial = listOf())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Zastąp Color.White odpowiednim kolorem
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(flightDatas) { flightData ->
                FlightDataItem(flightData)
            }
        }
    }
}

@Composable
fun FlightDataItem(flightData: FlightDatas) {
    // Przykładowy sposób wyświetlania danych
    Text(text = "Dane lotu: ${flightData.gpsLat}, ${flightData.gpsLng}, ${flightData.gpsAlt}, ${flightData.time}, ${flightData.temperature}, ${flightData.pressure}, ${flightData.height}, ${flightData.speed}, ${flightData.continuity}, ${flightData.state}")
}
