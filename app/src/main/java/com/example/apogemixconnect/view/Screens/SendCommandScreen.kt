package com.example.apogemixconnect.view.Screens.SendCommandScreen

// AndroidX and Compose imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apogemixconnect.ui.theme.Style.ButtonColor
import com.example.apogemixconnect.ui.theme.Style.ButtonShape
import com.example.apogemixconnect.ui.theme.Style.TextFieldHeight
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.TextFieldValue
import co.yml.charts.common.model.Point
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas

// Project specific imports
import com.example.apogemixconnect.viewmodel.WebSocketViewModel
import com.example.apogemixconnect.view.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.ui.theme.Style.*
import com.example.apogemixconnect.view.Screens.DataAnalysis.createPointsList
import com.example.apogemixconnect.view.Screens.DataAnalysis.getYValue
import com.example.apogemixconnect.viewmodel.DatabaseViewModel

@Composable
fun SendCommandScreen(
    viewModel: WebSocketViewModel,
    DBviewModel: DatabaseViewModel,
    navController: NavController,
    onClick: (String) -> Unit
) {
    val dataMap by viewModel.dataMap.observeAsState(mapOf())
    val namesList = dataMap.keys.toList()
    var selectedName by remember { mutableStateOf(namesList.firstOrNull().orEmpty()) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00072e))
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ConnectionStatus(viewModel, navController)
            DropdownDeviceNameSelector(DBviewModel, namesList, selectedName) { name -> selectedName = name }
            FrequencyInputBox(viewModel)
            CommandBox(selectedName, viewModel)
            TestButtonsBox(selectedName, viewModel)
        }
    }
}

@Composable
fun CommandBox(selectedName: String, viewModel: WebSocketViewModel) {
    val options = listOf("MOS_ON", "MOS_OFF", "MOS_CLK", "RECALIBRATE")
    var selectedYIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
    ) {
        Column {
            DropDownMenu(options, selectedYIndex) { newIndex ->
                selectedYIndex = newIndex
            }
            SendCommandButton(selectedName, options, selectedYIndex, viewModel)
        }
    }
}

@Composable
fun SendCommandButton(selectedName: String, options: List<String>, selectedYIndex: Int, viewModel: WebSocketViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    val confirmText = remember { mutableStateOf("") }

    Button(
        onClick = { showDialog.value = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25),
    ) {
        Text("SEND COMMAND")
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Command") },
            text = {
                Column {
                    Text("Type 'confirm' to send command:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = confirmText.value,
                        onValueChange = { confirmText.value = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (confirmText.value.lowercase() == "confirm") {
                            viewModel.sendCommand(selectedName, options[selectedYIndex])
                            showDialog.value = false
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun TestButtonsBox(selectedName: String, viewModel: WebSocketViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
    ) {
        Column {
            TestButton("TEST 1",selectedName, "TEST1" ,viewModel)
            TestButton("TEST 2",selectedName, "TEST2" ,viewModel)
        }
    }
}

@Composable
fun TestButton(text: String, selectedName: String, command: String, viewModel: WebSocketViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    val confirmText = remember { mutableStateOf("") }

    Button(
        onClick = { showDialog.value = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25),
    ) {
        Text(text)
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Command") },
            text = {
                Column {
                    Text("Type 'confirm' to send command:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = confirmText.value,
                        onValueChange = { confirmText.value = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (confirmText.value.lowercase() == "confirm") {
                            viewModel.sendCommand(selectedName, command)
                            showDialog.value = false
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}



@Composable
fun FrequencyInputBox(viewModel: WebSocketViewModel) {
    val inputText = remember { mutableStateOf("443") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            FrequencyInputField(inputText)
            SetFrequencyButton(viewModel, inputText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyInputField(inputText: MutableState<String>) {
    OutlinedTextField(
        value = inputText.value,
        onValueChange = { inputText.value = it },
        label = {
            Text(
                "Input Frequency",
                color = Color.White
            )
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun SetFrequencyButton(viewModel: WebSocketViewModel, inputText: MutableState<String>) {
    val showDialog = remember { mutableStateOf(false) }
    val confirmText = remember { mutableStateOf("") }

    Button(
        onClick = { showDialog.value = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25),
    ) {
        Text("Set Frequency")
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Frequency Change to ${inputText.value}") },
            text = {
                Column {
                    Text("Type 'confirm' to change frequency:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = confirmText.value,
                        onValueChange = { confirmText.value = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (confirmText.value.lowercase() == "confirm") {
                            viewModel.changeFrequency(inputText.value.toInt())
                            showDialog.value = false
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(options: List<String>, selectedYIndex: Int, onOptionSelected: (Int) -> Unit) {
    var expandedY by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedY,
                onExpandedChange = { expandedY = !expandedY },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = options[selectedYIndex],
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
                                onOptionSelected(index)
                                expandedY = false
                            },
                            text = { Text(text = option) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownDeviceNameSelector(
    DBviewModel: DatabaseViewModel,
    namesList: List<String>,
    selectedName: String,
    onSelectionChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedNameState by remember { mutableStateOf(selectedName) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .height(TextFieldHeight)
                    .fillMaxWidth()
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
}
