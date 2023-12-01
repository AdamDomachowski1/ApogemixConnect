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
import co.yml.charts.common.model.Point
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas

// Project specific imports
import com.example.apogemixconnect.viewmodel.WebSocketViewModel
import com.example.apogemixconnect.view.Screens.ConnectionScreen.ConnectionStatus
import com.example.apogemixconnect.ui.theme.Style.*
import com.example.apogemixconnect.view.Screens.DataAnalysis.createPointsList
import com.example.apogemixconnect.view.Screens.DataAnalysis.getYValue

@Composable
fun SendCommandScreen(
    viewModel: WebSocketViewModel,
    navController: NavController,
    onClick: (String) -> Unit
) {
    val inputText = remember { mutableStateOf("443") }
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
            FrequencyInputBox(inputText, viewModel)
            CommandBox(viewModel)
            TestButtonsBox(viewModel)
        }
    }
}

@Composable
fun CommandBox(viewModel: WebSocketViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
    ) {
        Column {
            DropDownMenu()
            SendCommandButton(viewModel)
        }
    }
}

@Composable
fun SendCommandButton(viewModel: WebSocketViewModel) {
    Button(
        onClick = { viewModel.changeFrequency(443) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25),
    ) {
        Text("SEND COMMAND")
    }
}

@Composable
fun TestButtonsBox(viewModel: WebSocketViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
    ) {
        Column {
            TestButton("TEST 1", viewModel)
            TestButton("TEST 2", viewModel)
        }
    }
}

@Composable
fun TestButton(text: String, viewModel: WebSocketViewModel) {
    Button(
        onClick = { viewModel.changeFrequency(443) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25),
    ) {
        Text(text)
    }
}


@Composable
fun FrequencyInputBox(inputText: MutableState<String>, viewModel: WebSocketViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            FrequencyInputField(inputText)
            SetFrequencyButton(viewModel)
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
fun SetFrequencyButton(viewModel: WebSocketViewModel) {
    Button(
        onClick = { viewModel.changeFrequency(443) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25),
    ) {
        Text("Set Frequency")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu() {
    var expandedY by remember { mutableStateOf(false) }
    val options = listOf("MOS_ON", "MOS_OFF", "MOS_CLK", "RECALIBRATE")
    var selectedYIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            // Menu dla osi Y
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
                                selectedYIndex = index
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