package com.example.apogemixconnect.model.Data.FlightDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights_table")
data class Flight(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0, //unike Id
    val name: String, //name of Apogemix (has to be editable)
    val date: String, //Storing datas in milisceonds since ..
)