package com.example.apogemixconnect.model.Data.FlightDataDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDatasDao {

    @Insert
    suspend fun insertFlightData(flightData: FlightDatas)
    @Insert
    suspend fun insertAllFlightData(flightDataList: List<FlightDatas>)
    @Delete
    suspend fun deleteFlightData(flightData: FlightDatas)
    @Update
    suspend fun updateFlightData(flightData: FlightDatas)

    @Query("SELECT * FROM flightDatas_table WHERE flightId = :flightId")
    fun getFlightDatasByUid(flightId: Int): Flow<List<FlightDatas>>
}