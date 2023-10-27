package com.example.apogemixconnect.ui.theme.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen(onClick: (String) -> Unit){
    Column {
        Text(text = "Settings")
        Button(onClick = { onClick("ConnectionScreen") }) {
            Text(text = "ConnectionScreen")
        }
    }
}