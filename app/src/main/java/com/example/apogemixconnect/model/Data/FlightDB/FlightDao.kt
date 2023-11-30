package com.example.apogemixconnect.model.Data.FlightDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Insert
    suspend fun insert(flight: Flight): Long
    @Delete
    suspend fun delete(flights: List<Flight>)
    @Update
    suspend fun update(flights: Flight)
    @Query("SELECT * FROM flights_table")
    fun getAll(): Flow<List<Flight>>
    @Query("DELETE FROM flights_table")
    suspend fun dropDatabase()
    @Query("DELETE FROM flights_table WHERE uid = :flightId")
    suspend fun deleteFlightById(flightId: Int)

}