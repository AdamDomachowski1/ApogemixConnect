import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatas
import com.example.apogemixconnect.model.Data.FlightDataDB.FlightDatasDao
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
class FlightDatasDaoTest {

    private lateinit var database: FlightDatabase
    private lateinit var flightDao: FlightDao
    private lateinit var flightDatasDao: FlightDatasDao

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
        flightDatasDao = database.flightDatasDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveFlightDataById() = runBlocking {
        val flight = Flight(
            uid = 1,
            name = "test",
            date = "13.12.2023"
        )
        val flightId = flightDao.insert(flight)

        val expectedFlightData = FlightDatas(
            flightId = flightId.toInt(),
            gpsLat = 123.456f,
            gpsLng = 789.123f,
            gpsAlt = 100.0f,
            time = 123456.0f,
            temperature = 25.0f,
            pressure = 1013.25f,
            altitude = 500.0f,
            speed = 100.0f,
            continuity = 1,
            state = 1
        )
        flightDatasDao.insertFlightData(expectedFlightData)

        val retrievedFlightData = flightDatasDao.getFlightDatasByUid(flightId.toInt()).first()

        assertThat(retrievedFlightData.size, equalTo(1)) // Sprawdź, czy jest tylko jedna pozycja danych lotu w bazie
        assertThat(retrievedFlightData[0].gpsLat, equalTo(expectedFlightData.gpsLat))
        assertThat(retrievedFlightData[0].gpsLng, equalTo(expectedFlightData.gpsLng))
        assertThat(retrievedFlightData[0].gpsAlt, equalTo(expectedFlightData.gpsAlt))
        assertThat(retrievedFlightData[0].time, equalTo(expectedFlightData.time))
        assertThat(retrievedFlightData[0].temperature, equalTo(expectedFlightData.temperature))
        assertThat(retrievedFlightData[0].pressure, equalTo(expectedFlightData.pressure))
        assertThat(retrievedFlightData[0].altitude, equalTo(expectedFlightData.altitude))
        assertThat(retrievedFlightData[0].speed, equalTo(expectedFlightData.speed))
        assertThat(retrievedFlightData[0].continuity, equalTo(expectedFlightData.continuity))
        assertThat(retrievedFlightData[0].state, equalTo(expectedFlightData.state))
    }

}
