package com.example.apogemixconnect.view.Screens.MainPage

// Android and Compose imports
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.provider.Settings

// ViewModel imports
import com.example.apogemixconnect.viewmodel.WebSocketViewModel

// Resource and style imports
import com.example.apogemixconnect.R
import com.example.apogemixconnect.ui.theme.Style.*

@Composable
fun MainPage(viewModel: WebSocketViewModel, navController: NavController, onClick: (String) -> Unit = {}) {
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
            InfoTextField()
            Spacer(modifier = Modifier.height(16.dp))
            SettingsButton(navController)
            Spacer(modifier = Modifier.height(16.dp))
            LaunchButton(onClick = { onClick("ConnectionScreen") })
        }
    }
}

@Composable
fun SettingsButton(navController: NavController) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .width(300.dp)
            .height(ButtonHeight)
            .padding(bottom = 1.dp),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
        onClick = {
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
    ) {
        Text(text = "WiFi Settings")
    }
}

@Composable
fun LogoImage(image: Painter) {
    Image(
        painter = image,
        contentDescription = "Logo",
        modifier = Modifier.size(LogoSize)
    )
}

@Composable
fun InfoTextField() {
    Text(
        text = "Before launching the application, make sure you are connected to the Apogemix device.",
        color = Color.White,
        modifier = Modifier
            .padding(16.dp)
            .width(300.dp)
    )
}

@Composable
fun LaunchButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .width(300.dp)
            .height(ButtonHeight)
            .padding(bottom = 1.dp),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
        onClick = onClick
    ) {
        Text(text = "Launch App!")
    }
}
