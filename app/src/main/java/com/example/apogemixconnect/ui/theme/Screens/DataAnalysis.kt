package com.example.apogemixconnect.ui.theme.Screens.DataAnalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.apogemixconnect.ui.theme.Screens.MainPage.BackgroundColor
import com.example.apogemixconnect.viewmodel.DatabaseViewModel
import com.example.apogemixconnect.viewmodel.WebSocketViewModel


@Composable
fun DataAnalysis(
    viewModel: WebSocketViewModel,
    DBviewModel: DatabaseViewModel,
    navController: NavController,
    param: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = param)
    }
}