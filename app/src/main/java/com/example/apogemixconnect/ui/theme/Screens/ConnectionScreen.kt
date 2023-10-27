package com.example.apogemixconnect.ui.theme.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConnectionScreen(onClick: (String) -> Unit) {
    Column {
        Text(text = "ConnectionScreen")
        Button(onClick = { onClick("detail") }) {
            Text(text = "Detail")
        }
        Button(onClick = { onClick("settings") }) {
            Text(text = "Settings")
        }
    }
}