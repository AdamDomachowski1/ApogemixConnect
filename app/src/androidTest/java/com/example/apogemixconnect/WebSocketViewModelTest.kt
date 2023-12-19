package com.example.apogemixconnect
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.apogemixconnect.model.DataMapRepository
import com.example.apogemixconnect.model.WebSocketListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import com.example.apogemixconnect.viewmodel.WebSocketViewModel

@ExperimentalCoroutinesApi
class WebSocketViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WebSocketViewModel

    @Mock
    private lateinit var mockOkHttpClient: OkHttpClient

    @Mock
    private lateinit var mockWebSocket: WebSocket

    @Mock
    private lateinit var webSocketListener: WebSocketListener

    @Mock
    private lateinit var socketStatusObserver: Observer<Boolean>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = WebSocketViewModel().apply {
            okHttpClient = mockOkHttpClient
            webSocket = mockWebSocket
            this.webSocketListener = webSocketListener
        }
        viewModel.socketStatus.observeForever(socketStatusObserver)
    }

    @Test
    fun connect_whenNotConnected_shouldInitiateConnection() {
        // Ustawienie warunku początkowego
        `when`(viewModel.socketStatus.value).thenReturn(false)
        `when`(mockOkHttpClient.newWebSocket(any(Request::class.java), any(WebSocketListener::class.java))).thenReturn(mockWebSocket)

        // Wykonanie metody
        viewModel.connect("ws://example.com")

        // Weryfikacja
        verify(mockOkHttpClient).newWebSocket(any(Request::class.java), any(WebSocketListener::class.java))
        verify(socketStatusObserver).onChanged(true)
    }

    @Test
    fun disconnect_whenConnected_shouldCloseConnection() = runBlockingTest {
        // Załóż, że socket jest połączony
        `when`(viewModel.socketStatus.value).thenReturn(true)

        // Wykonanie metody
        viewModel.disconnect()

        // Weryfikacja
        verify(mockWebSocket).close(1000, "Canceled manually.")
        verify(socketStatusObserver).onChanged(false)
    }

    // Dodaj więcej testów dla innych metod
}
