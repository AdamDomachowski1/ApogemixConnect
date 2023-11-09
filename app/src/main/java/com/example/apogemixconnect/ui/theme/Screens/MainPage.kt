package com.example.apogemixconnect.ui.theme.Screens.MainPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apogemixconnect.viewmodel.MainViewModel
import com.example.apogemixconnect.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Devices
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.apogemixconnect.viewmodel.DatabaseViewModel


// Define constants for colors and dimensions
private val  BackgroundColor = Color(0xFF00072e)
private val  ButtonColor = Color(0xFF6A205E)
private val  ButtonWidth = 280.dp
private val  ButtonHeight = 50.dp
private val  ButtonShape = RoundedCornerShape(20)
private val LogoSize = 500.dp

@Composable
fun MainPage(
    viewModel: MainViewModel,
    navController: NavController,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoImage(painterResource(id = R.mipmap.apogemixlogo))
            //tutaj doać łączenie z WiFi
            InfoTextField()
            Spacer(modifier = Modifier.height(16.dp))
            LaunchButton(onClick = { onClick("ConnectionScreen") })
        }
    }
}

@Composable
fun LogoImage(image: Painter) {
    Image(
        painter = image,
        contentDescription = "Logo",
        modifier = Modifier
            .size(LogoSize)
    )
}

@Composable
fun InfoTextField() {
    Text(
        text = "Before launching the application, make sure you are connected to the Apogemix device.",
        color = Color.White, 
        modifier = Modifier
            .padding(16.dp)
            .width(ButtonWidth)
    )
}


@Composable
fun LaunchButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .width(ButtonWidth)
            .height(ButtonHeight)
            .padding(bottom = 1.dp),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
        onClick = onClick
    ) {
        Text(text = "Launch App!")
    }
}

//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//fun MainPagePreview() {
//    MainPage(viewModel = viewModel(), navController = rememberNavController())
//}
