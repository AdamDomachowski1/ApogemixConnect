package com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.viewmodel.MainViewModel

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
fun ReciverDataScreen(viewModel: MainViewModel, navController: NavController, onClick: (String) -> Unit) {
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
            DropdownNameSelector(namesList, expanded, selectedName) { name, isExpanded ->
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
    namesList: List<String>,
    expanded: Boolean,
    selectedName: String,
    onSelectionChange: (String, Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Row {
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
                    .weight(2f),
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
                .fillMaxWidth(),
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

@Composable
fun DisplayFlightData(viewModel: MainViewModel, name: String) {
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val data = dataMap[name]?.split(";").orEmpty()

    if (data.isNotEmpty()) {
        FlightDataColumn(data, name)
    } else {
        NoDataText(name)
    }
}

@Composable
fun FlightDataColumn(data: List<String>, name: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        data.forEachIndexed { index, value ->
            FlightDataRow(label = "Data $index", value = value)
        }
    }
}

@Composable
fun NoDataText(name: String) {
    Text(
        "No data available for $name",
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun FlightDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Text(text = value, modifier = Modifier.weight(1f))
    }
}


