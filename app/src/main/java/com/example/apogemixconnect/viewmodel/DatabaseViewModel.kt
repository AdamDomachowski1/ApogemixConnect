package com.example.apogemixconnect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apogemixconnect.Data.FlightDB.Flight
import com.example.apogemixconnect.model.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DatabaseViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = DatabaseRepository(app.applicationContext)

    private val _nowRecording = mutableListOf<String>()
    // Public read-only view of nowRecording
    val nowRecording: List<String> = _nowRecording

    init{
        CoroutineScope(Dispatchers.IO).launch {
            repo.dropDatabase()
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
        val flight = Flight(name = name, date = formattedDate, dataId = 1)
        CoroutineScope(viewModelScope.coroutineContext).launch {
            repo.insertAll(listOf(flight))
        }
    }
}