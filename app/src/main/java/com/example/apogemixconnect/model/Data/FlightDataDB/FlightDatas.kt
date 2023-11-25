package com.example.apogemixconnect.model.Data.FlightDataDB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.apogemixconnect.model.Data.FlightDB.Flight


@Entity(tableName = "flightDatas_table")
data class FlightDatas(
    @PrimaryKey(autoGenerate = true) val dataId: Int = 0, //unike Id
    val flightId: Int,
    val gpsLat: Float,
    val gpsLng: Float,
    val gpsAlt: Float,
    val time: Int,
    val temperature: Float,
    val pressure: Float,
    val height: Float,
    val speed: Float,
    val continuity: Int,
    val state: Int,
    )
