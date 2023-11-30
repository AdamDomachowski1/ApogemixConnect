package com.example.apogemixconnect.view.Screens.DataAnalysis

// Android imports
import android.util.Log

// Compose imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Navigation imports
import androidx.navigation.NavController

// ViewModel imports
import com.example.apogemixconnect.viewmodel.DatabaseViewModel

// Model imports
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas

// Chart imports
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*

// Styles
import com.example.apogemixconnect.ui.theme.Style.*
import com.example.apogemixconnect.view.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.viewmodel.WebSocketViewModel

@Composable
fun DataAnalysis(
    viewModel: WebSocketViewModel,
    DBviewModel: DatabaseViewModel,
    navController: NavController,
    UID: Int,
    name: String,
    date: String
) {
    var deleteCommand = true
    val flightDatas by DBviewModel.getFlightDatasByUid(UID).collectAsState(initial = listOf())
    val points = remember(flightDatas) {
        mutableStateOf(createPointsList(flightDatas, { it.height }))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00072e)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            ConnectionStatus(viewModel, navController)
            DropDownMenu(points, flightDatas, DBviewModel, UID)
            FlightInfo(name,date)
            LineChartScreen(points.value)
        }
    }
}

@Composable
fun FlightInfo(name: String, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Dodatkowy padding dla całego wiersza, jeśli potrzebny
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(Color.Gray) // Szare tło dla Box z tekstem
                .padding(4.dp), // Padding wewnątrz Box
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Name: $name",
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .background(Color.Gray) // Szare tło dla drugiego Box
                .padding(4.dp), // Padding wewnątrz Box
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Date: $date",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(points: MutableState<List<Point>>, flightDatas: List<FlightDatas>,DBviewModel: DatabaseViewModel, uid: Int) {
    var expandedY by remember { mutableStateOf(false) }
    val options = listOf("Altitude", "Speed", "Temperature", "Pressure")
    var selectedYIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            // Menu dla osi Y
            ExposedDropdownMenuBox(
                expanded = expandedY,
                onExpandedChange = { expandedY = !expandedY },
                modifier = Modifier.fillMaxWidth() // Zajmuje całą szerokość
            ) {
                TextField(
                    value = " Yaxis: ${options[selectedYIndex]} ",
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedY) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(5.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedY,
                    onDismissRequest = { expandedY = false },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    options.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedYIndex = index
                                expandedY = false
                                // Aktualizacja danych dla osi Y
                                points.value = createPointsList(flightDatas,
                                    { flightData -> getYValue(flightData, selectedYIndex) }
                                )
                            },
                            text = { Text(text = option) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { DBviewModel.deleteFlight(uid)},
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Delete")
            }
        }
    }
}

@Composable
fun LineChartScreen(pointsData: List<Point>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (pointsData.isNotEmpty()) {
            // Znajdowanie maksymalnej i minimalnej wartości Y
            val maxY = pointsData.maxOfOrNull { it.y } ?: 0f
            val minY = pointsData.minOfOrNull { it.y } ?: 0f

            // Inicjalizacja danych osi X
            val xAxisData = AxisData.Builder()
                .axisStepSize(100.dp)
                .steps(pointsData.size - 1)
                .labelData { i -> i.toString() }
                .labelAndAxisLinePadding(15.dp)
                .build()

            // Inicjalizacja danych osi Y
            val yAxisData = AxisData.Builder()
                .steps(5) // Liczba kroków (można dostosować)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val labelValue = minY + (maxY - minY) / 5 * i
                    String.format("%.2f", labelValue) // Formatowanie wartości do dwóch miejsc po przecinku
                }
                .build()

            // Inicjalizacja danych wykresu liniowego
            val lineChartData = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = pointsData,
                            LineStyle(
                                lineType = LineType.Straight(isDotted = false)
                            ),
                            IntersectionPoint(),
                            SelectionHighlightPoint(),
                            ShadowUnderLine(),
                            SelectionHighlightPopUp()
                        )
                    ),
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines(),
                backgroundColor = Color.White
            )

            // Renderowanie wykresu liniowego z dostarczonymi danymi
            LineChart(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                lineChartData = lineChartData
            )
        } else {
            Text("No data available to display the chart.")
        }
    }
}

fun createPointsList(flightDatas: List<FlightDatas>, getYValue: (FlightDatas) -> Float): List<Point> {
    return flightDatas.map { flightData ->
        val timeInSeconds = flightData.time / 1000f // Konwersja na sekundy
        val point = Point(
            x = timeInSeconds,
            y = getYValue(flightData)
        )
        Log.d("createPointsList", "Point created: x=${point.x}, y=${point.y}")
        point
    }
}

fun getYValue(flightData: FlightDatas, selectedYIndex: Int): Float {
    return when (selectedYIndex) {
        0 -> flightData.height
        1 -> flightData.speed
        2 -> flightData.temperature
        3 -> flightData.pressure
        else -> 0f // Domyślna wartość lub inna logika
    }
}
