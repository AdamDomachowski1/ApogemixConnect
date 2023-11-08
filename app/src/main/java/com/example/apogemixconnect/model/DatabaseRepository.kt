package com.example.apogemixconnect.model


import android.content.Context;
import com.example.apogemixconnect.Data.FlightDB.Flight
import com.example.apogemixconnect.Data.FlightDB.FlightDao
import com.example.apogemixconnect.Data.FlightDB.FlightDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DatabaseRepository(context:Context): FlightDao {

    private val dao = FlightDb.getInstance(context).flightDao()

    override suspend fun insertAll(flights: List<Flight>) = withContext(Dispatchers.IO){
        dao.insertAll(flights)
    }

    override suspend fun delete(flights: List<Flight>) = withContext(Dispatchers.IO) {
        dao.delete(flights)
    }

    override suspend fun update(flights: Flight) = withContext(Dispatchers.IO) {
        dao.update(flights)
    }

    override fun getAll(): Flow<List<Flight>> {
        return dao.getAll()
    }

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        dao.dropDatabase()
    }

}
