package com.example.apogemixconnect.model.Data.FlightDataDB

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.apogemixconnect.model.Data.FlightDB.Flight


@Entity(
    tableName = "flightDatas_table",
    foreignKeys = [
        ForeignKey(
            entity = Flight::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("flightId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["flightId"])]
)
data class FlightDatas(
    @PrimaryKey(autoGenerate = true) val dataId: Int = 0,
    val flightId: Int,
    val gpsLat: Float,
    val gpsLng: Float,
    val gpsAlt: Float,
    val time: Float,
    val temperature: Float,
    val pressure: Float,
    val altitude: Float,
    val speed: Float,
    val continuity: Int,
    val state: Int,
    )
