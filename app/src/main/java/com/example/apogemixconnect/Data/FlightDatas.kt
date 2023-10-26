package com.example.apogemixconnect.Data

data class FlightDatas(
    val name: String,
    val gpsLat: Float,
    val gpsLng: Float,
    val gpsAlt: Float,
    val time: Int,
    val temperature: Float,
    val pressure: Float,
    val height: Float,
    val speed: Float,
    val continuity: Int,
    val state: Int
)
