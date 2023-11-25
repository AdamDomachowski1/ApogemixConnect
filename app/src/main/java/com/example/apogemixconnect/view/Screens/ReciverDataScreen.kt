package com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apogemixconnect.model.Data.FlightDB.Flight
import com.example.apogemixconnect.viewmodel.WebSocketViewModel
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.viewmodel.DatabaseViewModel

// Constants for UI Design
private val BoxHeight = 50.dp
private val TextFieldHeight = 60.dp
private val ButtonHeight = 50.dp
private val HorizontalPadding = 16.dp
private val ButtonShape = RoundedCornerShape(25)
private val ArrowIconSize = 40.dp
private val SpacerWidth = 40.dp
val ButtonColor = Color(0xFF6A205E)
val BackgroundColor = Color(0xFF00072e)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciverDataScreen(viewModel: WebSocketViewModel, DBviewModel: DatabaseViewModel, navController: NavController, onClick: (String) -> Unit) {
    // States
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val namesList = dataMap.keys.toList()
    var expanded by remember { mutableStateOf(false) }
    var selectedName by remember { mutableStateOf(namesList.firstOrNull().orEmpty()) }

    // Box UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center // Center the content in the box
    ) {
        // Screen UI inside Box
        Column(
            modifier = Modifier.fillMaxSize(), // Add a background color to the column
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConnectionStatus(viewModel, navController)
            DropdownNameSelector(DBviewModel, namesList, expanded, selectedName) { name, isExpanded ->
                selectedName = name
                expanded = isExpanded
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
    expanded: Boolean,
    selectedName: String,
    onSelectionChange: (String, Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .height(TextFieldHeight),
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(TextFieldHeight),
                shape = RoundedCornerShape(4.dp), // Adjust the shape as needed for your ButtonShape
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
            
            Spacer(modifier = Modifier.width(10.dp) )

            OutlinedTextField(
                value = selectedName,
                onValueChange = { },
                label = { Text("Select Apogemix") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        Modifier.clickable { onSelectionChange(selectedName, !expanded) },
                        tint = Color.White
                    )
                },
                modifier = Modifier
                    .weight(3f)
                    .height(TextFieldHeight),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                )
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onSelectionChange(selectedName, false) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            namesList.forEach { name ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = { onSelectionChange(name, false) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayFlightData(viewModel: WebSocketViewModel, name: String) {
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val data = dataMap[name]?.split(";").orEmpty()
   Column(modifier = Modifier.padding(8.dp)) {
       FlightRow("GPS Coordinates", value = if (data.isNotEmpty()) "${data[0]} ; ${data[1]}" else "N/A")
       FlightRow("GPS Altitude", value = if (data.isNotEmpty()) "${data[2]} m" else "N/A")
       FlightRow("Calibrated Altitude", value = if (data.isNotEmpty()) "${data[6]} m" else "N/A")
       FlightRow("Time", value = if (data.isNotEmpty()) "${data[3]} ms" else "N/A")
       FlightRow("Temperature", value = if (data.isNotEmpty()) "${data[4]} \u00B0C" else "N/A")
       FlightRow("Pressure", value = if (data.isNotEmpty()) "${data[5]} Pa" else "N/A")
       FlightRow("Vertical Velocity", value = if (data.isNotEmpty()) "${data[7]} m/s" else "N/A")
       FlightRow("Continuity", value = if (data.isNotEmpty()) "${data[8]}"  else "N/A")
       FlightRow("Status", value = if (data.isNotEmpty()) "${data[9]}"  else "N/A")
    }
}


@Composable
fun FlightRow(label: String, value: String){
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .padding(4.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .weight(1f))

            Text(text = value,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally) // Wyśrodkowanie poziome
                    .padding(8.dp)
                    .weight(1f))
        }
    }
}
