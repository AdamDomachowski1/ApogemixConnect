package com.example.apogemixconnect.ui.theme.Screens.ReciverDataScreen

// AndroidX and Compose imports
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Project specific imports
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciverDataScreen(viewModel: MainViewModel, onClick: (String) -> Unit) {
    // State and variables
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val namesList = dataMap.keys.toList()
    var expanded by remember { mutableStateOf(false) }
    var selectedName by remember { mutableStateOf(namesList.firstOrNull() ?: "") }

    // Main column for the screen
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConnectionStatus(viewModel)
        DropdownNameSelector(namesList, expanded, selectedName) { name, isExpanded ->
            selectedName = name
            expanded = isExpanded
        }
        DisplayFlightData(viewModel, selectedName)
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
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = { },
            label = { Text("Select Name") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    Modifier.clickable { onSelectionChange(selectedName, !expanded) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onSelectionChange(selectedName, false) },
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()
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
    val dataConvertedToArray = dataMap[name]?.split(";") ?: listOf()

    if (dataConvertedToArray.isNotEmpty()) {
        FlightDataColumn(dataConvertedToArray, name)
    } else {
        NoDataText(name)
    }
}

@Composable
fun FlightDataColumn(dataConvertedToArray: List<String>, name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        dataConvertedToArray.forEachIndexed { index, value ->
            FlightDataRow(label = "Data $index", value = value)
        }
    }
}

@Composable
fun NoDataText(name: String) {
    Text(
        "No data available for the specified name: $name",
        Modifier.padding(16.dp)
    )
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
