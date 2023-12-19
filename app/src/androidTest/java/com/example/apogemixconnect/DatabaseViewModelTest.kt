import android.app.Application
import android.content.Context
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apogemixconnect.viewmodel.DatabaseViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DatabaseViewModelTest {
    private lateinit var viewModel: DatabaseViewModel

    @Before
    fun setup() {
        // Pobieranie kontekstu aplikacji
        val context = ApplicationProvider.getApplicationContext<Context>()
        val application = ApplicationProvider.getApplicationContext() as Application
        viewModel = DatabaseViewModel(application)
    }

    @Test
    fun addToNowRecording_addsItem() {
        val testString = "testString"
        viewModel.addToNowRecording(testString)

        // Sprawdzanie, czy element został dodany
        assertTrue(viewModel.nowRecording.contains(testString))
    }

    @Test
    fun removeFromNowRecording_removesItem() {
        val testString = "testString"
        viewModel.addToNowRecording(testString)
        viewModel.removeFromNowRecording(testString)

        // Sprawdzanie, czy element został usunięty
        assertFalse(viewModel.nowRecording.contains(testString))
    }

    @Test
    fun isStringInList_returnsCorrectValue() {
        val testString = "testString"
        viewModel.addToNowRecording(testString)

        // Sprawdzanie, czy metoda zwraca prawidłowy wynik
        assertTrue(viewModel.isStringInList(testString))
        assertFalse(viewModel.isStringInList("nonExistingString"))
    }

    // Możesz dodać więcej testów dla innych funkcji ViewModel
}
