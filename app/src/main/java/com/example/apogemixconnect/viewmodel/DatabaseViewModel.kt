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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class DatabaseViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = DatabaseRepository(app.applicationContext)

    fun getContacts() : Flow<List<Flight>> {
        println("fupaaaa")
        return repo.getAll()
    }

    fun napiszxd(): String {
        return("XD")
    }

    init{
        CoroutineScope(Dispatchers.IO).launch {
            repo.dropDatabase()
            populateDatabse()
        }
        populateDatabse()
    }

    private fun populateDatabse(){
        repeat(5){
            val time = System.currentTimeMillis()
            val flight = Flight(name = "$time", date = time.toLong(), dataId = time.toInt())
            CoroutineScope(viewModelScope.coroutineContext).launch {
                repo.insertAll(listOf(flight))
            }
        }
    }
}