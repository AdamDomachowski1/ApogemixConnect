package com.example.apogemixconnect.Data.FlightDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Flight::class], version = 2, exportSchema = false)
abstract class FlightDatabase: RoomDatabase() {
    abstract fun flightDao(): FlightDao
}

object FlightDb{ //Singleton - czyli jeden obiekt współdzielony wszędzie

    private var db: FlightDatabase? = null //zabezpieczenie przed dwoma instacjami database
    fun getInstance(context: Context): FlightDatabase {
        if(db == null){
            db = Room.databaseBuilder(
                context,
                FlightDatabase::class.java,
                "flight-database"
            ).build()
        }
        return db!!
    }
}