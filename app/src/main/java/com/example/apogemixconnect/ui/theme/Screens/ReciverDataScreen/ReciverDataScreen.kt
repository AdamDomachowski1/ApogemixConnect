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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Project specific imports
import com.example.apogemixconnect.ui.theme.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciverDataScreen(viewModel: MainViewModel, navController: NavController, onClick: (String) -> Unit) {
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
        ConnectionStatus(viewModel, navController)
        DropdownNameSelector(namesList, expanded, selectedName) { name, isExpanded ->
            selectedName = name
            expanded = isExpanded
        }
        InputField(viewModel)
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
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = { },
            label = { Text("Select Name", color = Color.White) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    Modifier.clickable { onSelectionChange(selectedName, !expanded) },
                    tint = Color.White
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White), // This will set the text color to white
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onSelectionChange(selectedName, false) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            namesList.forEach { name ->
                DropdownMenuItem(
                    text = { Text(name, color = Color.White) },
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
        color = Color.White
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
        Text(text = label, modifier = Modifier.weight(1f), color = Color.White)
        Text(text = value, modifier = Modifier.weight(1f), color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(viewModel: MainViewModel) {
    var value by remember { mutableStateOf("443") }

    // We use Box to center the Row in the screen.
    Box(
        contentAlignment = Alignment.Center, // This centers the Row in the Box.
    ) {
        // Row to contain the TextField and Button side by side.
        Row(
            modifier = Modifier
                .width(IntrinsicSize.Max) // This makes the Row size to its children's maximum intrinsic width.
                .wrapContentHeight(), // This makes the Row height to wrap its content.
            verticalAlignment = Alignment.CenterVertically, // This centers the children vertically in the Row.
            horizontalArrangement = Arrangement.spacedBy(8.dp) // This adds space between the children horizontally.
        ) {
            TextField(
                value = value,
                onValueChange = { value = it },
                label = {
                    Text(
                        "Set Frequency",
                        textAlign = TextAlign.Center, // Aligns the label text when it is displayed above the TextField.
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth() // Ensures the label takes full width inside the TextField.
                    )
                },
                singleLine = true, // Use this instead of maxLines = 1 for single-line TextField.
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                modifier = Modifier
                    .weight(1f) // Makes the TextField expand to fill available space
                    .height(IntrinsicSize.Min), // This ensures the height is just enough for its content.
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, // To also remove the underline indicator color
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            TextButton(
                onClick = {
                    viewModel.changeFrequency(value.toInt())
                },
                modifier = Modifier
                    .weight(1f) // Makes the Button expand to fill available space
                    .height(48.dp), // You can set a fixed height for the button.
                shape = RectangleShape // This makes the corners sharp, making it a rectangle.
            ) {
                Text("Set", color = Color.White)
            }

        }
    }
}

