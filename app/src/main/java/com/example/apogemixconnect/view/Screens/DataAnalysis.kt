package com.example.apogemixconnect.view.Screens.DataAnalysis

// Android imports
import android.util.Log

// Compose imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue

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
        mutableStateOf(createPointsList(flightDatas, { it.altitude }))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00072e)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            ConnectionStatus(viewModel, navController)
            DropDownMenu(points, flightDatas)
            FlightInfo(name, date, DBviewModel, UID)
            LineChartScreen(points.value)
        }
    }
}

@Composable
fun FlightInfo(name: String, date: String, DBviewModel: DatabaseViewModel, uid: Int) {
    var showDialog by remember { mutableStateOf(false) }
    var deleteText by remember { mutableStateOf(TextFieldValue("")) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Do you want to delete ${name}?") },
            text = {
                Column {
                    Text("Type 'delete', to confirm:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = deleteText,
                        onValueChange = { deleteText = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (deleteText.text.lowercase() == "delete") {
                            DBviewModel.deleteFlight(uid)
                            showDialog = false
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .background(Color.Gray)
                .weight(3f)
                .height(40.dp)
        ) {
            Text(
                text = "$name / $date",
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            shape = RoundedCornerShape(25),
        ) {
            Text(text = "Delete")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(points: MutableState<List<Point>>, flightDatas: List<FlightDatas>) {
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
            ExposedDropdownMenuBox(
                expanded = expandedY,
                onExpandedChange = { expandedY = !expandedY },
                modifier = Modifier.fillMaxWidth()
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
                                points.value = createPointsList(flightDatas,
                                    { flightData -> getYValue(flightData, selectedYIndex) }
                                )
                            },
                            text = { Text(text = option) }
                        )
                    }
                }
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
            val maxY = pointsData.maxOfOrNull { it.y } ?: 0f
            val minY = pointsData.minOfOrNull { it.y } ?: 0f

            val xAxisData = AxisData.Builder()
                .axisStepSize(100.dp)
                .steps(pointsData.size - 1)
                .labelData { i -> i.toString() }
                .labelAndAxisLinePadding(15.dp)
                .build()

            val yAxisData = AxisData.Builder()
                .steps(5)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val labelValue = minY + (maxY - minY) / 5 * i
                    String.format("%.2f", labelValue)
                }
                .build()

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
        val timeInSeconds = flightData.time / 1000f
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
        0 -> flightData.altitude
        1 -> flightData.speed
        2 -> flightData.temperature
        else -> 0f
    }
}

