package com.example.apogemixconnect.model

import android.content.Context
import com.example.apogemixconnect.model.Data.FlightDB.Flight
import com.example.apogemixconnect.model.Data.FlightDB.FlightDao
import com.example.apogemixconnect.model.Data.FlightDb
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DatabaseRepository(context: Context) : FlightDao, FlightDatasDao {

    // Inicjalizacja DAO
    private val daoFlight = FlightDb.getInstance(context).flightDao()
    private val daoDatasFlight = FlightDb.getInstance(context).flightDatasDao()

    // Metody dla FlightDao
    override suspend fun insert(flight: Flight): Long = withContext(Dispatchers.IO) {
        daoFlight.insert(flight)
    }

    override suspend fun update(flights: Flight) = withContext(Dispatchers.IO) {
        daoFlight.update(flights)
    }

    override fun getAll(): Flow<List<Flight>> = daoFlight.getAll()

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        daoFlight.dropDatabase()
    }

    override suspend fun deleteFlightById(flightId: Int) = withContext(Dispatchers.IO) {
        daoFlight.deleteFlightById(flightId)
        //an once delete all data parameters connected with this
    }

    // Metody dla FlightDatasDao
    override suspend fun insertFlightData(flightData: FlightDatas) = withContext(Dispatchers.IO) {
        daoDatasFlight.insertFlightData(flightData)
    }


    override fun getFlightDatasByUid(flightId: Int): Flow<List<FlightDatas>> = daoDatasFlight.getFlightDatasByUid(flightId)
}
