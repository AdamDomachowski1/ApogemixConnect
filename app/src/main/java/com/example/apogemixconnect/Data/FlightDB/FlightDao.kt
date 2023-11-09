package com.example.apogemixconnect.Data.FlightDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    @Insert
    suspend fun insertAll(contacts: List<Flight>)

    @Delete
    suspend fun delete(contacts: List<Flight>)

    @Update
    suspend fun update(contacts: Flight)

    @Query("SELECT * FROM flights_table")
    fun getAll(): Flow<List<Flight>>

    @Query("DELETE FROM flights_table")
    suspend fun dropDatabase()

}