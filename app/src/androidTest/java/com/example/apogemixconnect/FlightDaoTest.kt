package com.example.apogemixconnect

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.apogemixconnect.model.Data.FlightDB.Flight
import com.example.apogemixconnect.model.Data.FlightDB.FlightDao
import com.example.apogemixconnect.model.Data.FlightDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlightDaoTest {

    private lateinit var database: FlightDatabase
    private lateinit var flightDao: FlightDao

    // Rule do natychmiastowego wykonania operacji na wątku głównym (wymagane dla testów LiveData)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // Inicjalizacja bazy danych w trybie pamięci
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FlightDatabase::class.java
        ).allowMainThreadQueries().build()

        flightDao = database.flightDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveFlight() = runBlocking {
        val flight = Flight(
            uid = 1,
            name = "test",
            date = "13.12.2023"
        )
        val flightId = flightDao.insert(flight)

        val retrievedFlight = flightDao.getAll().first()

        assertThat(retrievedFlight.size, equalTo(1)) // Sprawdź, czy jest tylko jedno lotnisko w bazie
        assertThat(retrievedFlight[0], equalTo(flight)) // Sprawdź, czy dane lotniska są identyczne
    }

    @Test
    fun updateFlight() = runBlocking {
        val flight = Flight(
            uid = 2,
            name = "test2",
            date = "14.12.2023"
        )
        val flightId = flightDao.insert(flight)

        // Zaktualizuj dane lotniska
        val updatedFlight = flight.copy(
            // Zaktualizuj odpowiednie pola
        )
        flightDao.update(updatedFlight)

        val retrievedFlight = flightDao.getAll().first()

        assertThat(retrievedFlight.size, equalTo(1))
        assertThat(retrievedFlight[0], equalTo(updatedFlight))
    }

    @Test
    fun deleteFlight() = runBlocking {
        val flight = Flight(
            uid = 1,
            name = "test",
            date = "13.12.2023"
        )

        val flightId = 1

        flightDao.deleteFlightById(flightId)

        val retrievedFlight = flightDao.getAll().first()

        assertThat(retrievedFlight.isEmpty(), equalTo(true))
    }
}
