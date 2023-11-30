package com.example.apogemixconnect.model.Data.FlightDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights_table")
data class Flight(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val name: String,
    val date: String,
)