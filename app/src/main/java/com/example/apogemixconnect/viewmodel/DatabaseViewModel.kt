package com.example.apogemixconnect.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apogemixconnect.model.Data.FlightDB.Flight
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas
import com.example.apogemixconnect.model.DataMapRepository
import com.example.apogemixconnect.model.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class DatabaseViewModel(app: Application) : AndroidViewModel(app) {
    val repo = DatabaseRepository(app.applicationContext)

    val dataMap: LiveData<Map<String, String>> = DataMapRepository.dataMap

    private val _nowRecording = mutableListOf<String>()
    // Public read-only view of nowRecording
    val nowRecording: List<String> = _nowRecording

    init{
        CoroutineScope(Dispatchers.IO).launch {
            //repo.dropDatabase()
        }
    }

    fun addToNowRecording(string: String){
        _nowRecording.add(string)
    }

    fun removeFromNowRecording(string: String) {
        _nowRecording.remove(string)
    }

    fun isStringInList(stringToCheck: String): Boolean {
        // No need to switch to the main thread as we are only reading the list
        return _nowRecording.contains(stringToCheck)
    }

    fun getFlights() : Flow<List<Flight>> {
        return repo.getAll()
    }

    fun addToDatabase(name: String){
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MMM.yyyy HH:mm")
        val formattedDate = currentDateTime.format(formatter)
        val flight = Flight(name = name, date = formattedDate)
        CoroutineScope(viewModelScope.coroutineContext).launch {
            val id = repo.insert(flight)
            Log.d("XD", "$id")
            recordDataWhileNameIsRecording(name, id.toInt())
        }
    }

    fun parseDataStringToFlightDatas(dataString: String, flightId: Int): FlightDatas? {
        val dataParts = dataString.split(";")
        if (dataParts.size != 10) {
            Log.e("DatabaseViewModel", "Nieprawidłowy format danych: $dataString")
            return null
        }

        return try {
            FlightDatas(
                flightId = flightId,
                gpsLat = dataParts[0].toFloat(),
                gpsLng = dataParts[1].toFloat(),
                gpsAlt = dataParts[2].toFloat(),
                time = dataParts[3].toFloat(),
                temperature = dataParts[4].toFloat(),
                pressure = dataParts[5].toFloat(),
                altitude = dataParts[6].toFloat(),
                speed = dataParts[7].toFloat(),
                continuity = dataParts[8].toInt(),
                state = dataParts[9].toInt()
            )
        } catch (e: NumberFormatException) {
            Log.e("DatabaseViewModel", "Błąd podczas przetwarzania danych: $dataString", e)
            null
        }
    }

    fun recordDataWhileNameIsRecording(name: String, flightId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            while (_nowRecording.contains(name)) {
                val dataString = DataMapRepository.dataMap.value?.get(name)
                dataString?.let {
                    val flightData = parseDataStringToFlightDatas(dataString, flightId)
                    flightData?.let {
                        repo.insertFlightData(flightData)
                    }
                }
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
    }

    fun getFlightDatasByUid(flightId: Int): Flow<List<FlightDatas>> {
        return repo.getFlightDatasByUid(flightId)
    }

    fun deleteFlight(flightId: Int) {
        viewModelScope.launch {
            repo.deleteFlightById(flightId)
        }
    }




}