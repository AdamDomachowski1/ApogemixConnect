package com.example.apogemixconnect.viewmodel


import android.util.Log
import androidx.lifecycle.*
import com.example.apogemixconnect.model.WebSocketListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MainViewModel : ViewModel() {


    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages = MutableLiveData<Pair<Boolean, String>>()
    val messages: LiveData<Pair<Boolean, String>> = _messages

    private val _dataMap = MutableLiveData<Map<String, String>>()
    val dataMap: LiveData<Map<String, String>> = _dataMap

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val webSocketListener = WebSocketListener(this)


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
        clearMessage()
        resetData()
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

    fun handleIncomingMessage(message: Pair<Boolean, String>) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _messages.value = message
        }
    }

    fun updateData(dataString: String) {
        Log.d("WebSocket", "Received data: $dataString")
        val parts = dataString.split(";", limit = 2)
        if (parts.size == 2) {
            val transmitterName = parts[0]
            val restOfData = parts[1]

            // Get a copy of the current map or create a new one if null
            val updatedMap = _dataMap.value?.toMutableMap() ?: mutableMapOf()

            // Update the map with the new data
            updatedMap[transmitterName] = restOfData

            // Post the updated map back to MutableLiveData
            _dataMap.postValue(updatedMap)
            Log.d("WebSocket", "restOfData: $restOfData")
        }
    }


    fun changeFrequency(freqMHz: Int){
        val client = OkHttpClient()

        val url = "http://192.168.4.1/?freqmhz=$freqMHz"



        val request = Request.Builder()
            .url(url)
            .build()

        // Wykonanie żądania w osobnym wątku, ponieważ nie można wykonywać operacji sieciowych w wątku UI.
        Thread {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    // Kod 200, wszystko poszło dobrze
                    Log.d("GETyyy","Frequency changed successfully to $freqMHz MHz")
                } else {
                    // Błąd podczas zmiany częstotliwości
                    Log.d("GETyyy","Failed to change frequency: ${response.message}")
                }
            }
        }.start()
    }


    fun clearMessage() = viewModelScope.launch(Dispatchers.Main) {
        _messages.value = Pair(false,"")
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    fun resetData() {
        _dataMap.postValue(emptyMap())
    }

}