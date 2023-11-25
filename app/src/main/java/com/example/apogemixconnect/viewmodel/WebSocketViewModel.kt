package com.example.apogemixconnect.viewmodel


import android.util.Log
import androidx.lifecycle.*
import com.example.apogemixconnect.model.DataMapRepository
import com.example.apogemixconnect.model.WebSocketListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class WebSocketViewModel : ViewModel() {

    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val okHttpClient = OkHttpClient()

    private var webSocket: WebSocket? = null

    private val webSocketListener = WebSocketListener(this)

    val dataMap: LiveData<Map<String, String>> = DataMapRepository.dataMap

    // Connect Function
    fun connect(url: String) {
        if (!_socketStatus.value!!) { // Tylko jeśli połączenie nie jest aktywne
            webSocket = okHttpClient.newWebSocket(createRequest(url), webSocketListener)
            Log.d("CUSTOM_TAG","Connection Try")
        }
    }

    // Disconnect Function
    fun disconnect() {
        webSocket?.close(1000, "Canceled manually.")
        resetDataInDataMap()
    }

    // Send Command Function
    fun sendCommand(command: String) {
        webSocket?.send(command)
    }

    private fun createRequest(adress: String): Request {
        return Request.Builder()
            .url(adress)
            .build()
    }

    fun updateDataInDataMap(dataString: String) {
        val parts = dataString.split(";", limit = 2)
        if (parts.size == 2) {
            val key = parts[0]
            val value = parts[1]
            DataMapRepository.updateDataMap(key, value)
        }
    }

    fun changeFrequency(freqMHz: Int) = viewModelScope.launch {
        if (_socketStatus.value == true) {
            val success = webSocketListener.changeFrequency(freqMHz)
            if (success) {
                Log.d("GETyyy","Frequency changed successfully to $freqMHz MHz")
            } else {
                Log.d("GETyyy","Failed to change frequency")
            }
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    fun resetDataInDataMap() {
        DataMapRepository.resetDataMap()
    }
}