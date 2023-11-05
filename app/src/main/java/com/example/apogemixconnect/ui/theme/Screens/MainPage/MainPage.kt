package com.example.apogemixconnect.ui.theme.Screens.MainPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Devices
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController


@Composable
fun MainPage(viewModel: MainViewModel, navController: NavController, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder for image
        val image: Painter = painterResource(id = R.mipmap.apogemixlogo) // Replace with your actual image resource
        Image(
            painter = image,
            contentDescription = "Photo",
            modifier = Modifier
                .size(500.dp) // Specify the size you want
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Add space between image and button

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 1.dp),
            shape = RoundedCornerShape(0),
            onClick = {
                onClick("ConnectionScreen")
            }
        ) {
            Text(text = "Launch App!")
        }
    }
}

