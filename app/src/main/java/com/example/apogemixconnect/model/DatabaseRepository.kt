package com.example.apogemixconnect.model


import android.content.Context;
import com.example.apogemixconnect.Data.FlightDB.Flight
import com.example.apogemixconnect.Data.FlightDB.FlightDao
import com.example.apogemixconnect.Data.FlightDB.FlightDb
import com.example.apogemixconnect.Data.FlightDataDB.FlightDatas
import com.example.apogemixconnect.Data.FlightDataDB.FlightDatasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DatabaseRepository(context: Context) : FlightDao, FlightDatasDao {
    private val daoFlight = FlightDb.getInstance(context).flightDao()
    private val daoDatasFlight = FlightDb.getInstance(context).flightDatasDao()

    override suspend fun updateFlightData(flightData: FlightDatas) = withContext(Dispatchers.IO) {
        daoDatasFlight.updateFlightData(flightData)
    }

    override suspend fun deleteFlightData(flightData: FlightDatas) = withContext(Dispatchers.IO) {
        daoDatasFlight.deleteFlightData(flightData)
    }

    override suspend fun insertAllFlightData(flightDataList: List<FlightDatas>) = withContext(Dispatchers.IO) {
        daoDatasFlight.insertAllFlightData(flightDataList)
    }

    override suspend fun insertFlightData(flightData: FlightDatas) = withContext(Dispatchers.IO) {
        daoDatasFlight.insertFlightData(flightData)
    }

    override suspend fun insert(flight: Flight): Long = withContext(Dispatchers.IO) {
        daoFlight.insert(flight)
    }

    override suspend fun delete(flights: List<Flight>) = withContext(Dispatchers.IO) {
        daoFlight.delete(flights)
    }

    override suspend fun update(flights: Flight) = withContext(Dispatchers.IO) {
        daoFlight.update(flights)
    }

    override fun getAll(): Flow<List<Flight>> = daoFlight.getAll()

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        daoFlight.dropDatabase()
    }

}
