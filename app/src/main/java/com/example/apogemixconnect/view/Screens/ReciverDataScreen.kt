package com.example.apogemixconnect.view.Screens.ReciverDataScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apogemixconnect.viewmodel.WebSocketViewModel
import com.example.apogemixconnect.viewmodel.DatabaseViewModel
import com.example.apogemixconnect.view.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.ui.theme.Style.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciverDataScreen(
    viewModel: WebSocketViewModel,
    DBviewModel: DatabaseViewModel,
    navController: NavController,
    onClick: (String) -> Unit
) {
    // States
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val namesList = dataMap.keys.toList()
    var selectedName by remember { mutableStateOf(namesList.firstOrNull().orEmpty()) }

    // Box UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Screen UI inside Box
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConnectionStatus(viewModel, navController)
            DropdownNameSelector(DBviewModel, namesList, selectedName) { name ->
                selectedName = name
            }
            DisplayFlightData(viewModel, selectedName)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownNameSelector(
    DBviewModel: DatabaseViewModel,
    namesList: List<String>,
    selectedName: String,
    onSelectionChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedNameState by remember { mutableStateOf(selectedName) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {


        Button(
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (DBviewModel.isStringInList(selectedName)) Color.Red else Color.DarkGray
            ),
            onClick = {
                if (DBviewModel.isStringInList(selectedName)) {
                    DBviewModel.removeFromNowRecording(selectedName)
                } else {
                    DBviewModel.addToDatabase(selectedName)
                    DBviewModel.addToNowRecording(selectedName)
                }
            }
        ) {
            Text(text = if (DBviewModel.isStringInList(selectedName)) "END" else "REC")
        }

        Spacer(modifier = Modifier.width(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .height(TextFieldHeight)
                .weight(3f)
        ) {
            TextField(
                value = selectedNameState,
                onValueChange = { selectedNameState = it },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                namesList.forEach { name ->
                    DropdownMenuItem(
                        onClick = {
                            selectedNameState = name
                            expanded = false
                            onSelectionChange(name)
                        },
                        text = { Text(text = name) }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayFlightData(
    viewModel: WebSocketViewModel,
    name: String
) {
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val data = dataMap[name]?.split(";").orEmpty()

    Column {
        FlightRow("GPS Coordinates", value = if (data.isNotEmpty()) "${data[0]} ${data[1]}" else "N/A")
        FlightRow("GPS Altitude", value = if (data.isNotEmpty()) "${data[2]} m" else "N/A")
        FlightRow("Calibrated Altitude", value = if (data.isNotEmpty()) "${data[6]} m" else "N/A")
        FlightRow("Time", value = if (data.isNotEmpty()) "${data[3]} ms" else "N/A")
        FlightRow("Temperature", value = if (data.isNotEmpty()) "${data[4]} \u00B0C" else "N/A")
        FlightRow("Pressure", value = if (data.isNotEmpty()) "${data[5]} Pa" else "N/A")
        FlightRow("Vertical Velocity", value = if (data.isNotEmpty()) "${data[7]} m/s" else "N/A")
        FlightRow("Continuity", value = if (data.isNotEmpty()) "${data[8]}" else "N/A")
        FlightRow("Status", value = if (data.isNotEmpty()) "${data[9]}" else "N/A")
    }
}


@Composable
fun FlightRow(
    label: String,
    value: String
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .padding(4.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .weight(1f)
            )

            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(8.dp)
                    .weight(1f)
            )
        }
    }
}
