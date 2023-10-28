package com.example.apogemixconnect.viewmodel


import androidx.lifecycle.*
import com.example.apogemixconnect.Data.FlightDatas
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

    private val _flightData = MutableLiveData<FlightDatas>()
    val flightData: LiveData<FlightDatas> = _flightData

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val webSocketListener = WebSocketListener(this)

    // Connect Function
    fun connect(url: String) {
        webSocket = okHttpClient.newWebSocket(createRequest(url), webSocketListener)
    }

    // Disconnect Function
    fun disconnect() {
        webSocket?.close(1000, "Canceled manually.")
        clearMessage()
        resetFlightData()
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

    fun setFlightData(data: FlightDatas) = viewModelScope.launch(Dispatchers.Main) {
        _flightData.value = data
    }

    fun clearMessage() = viewModelScope.launch(Dispatchers.Main) {
        _messages.value = Pair(false,"")
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    fun splitDatas(rawData: String): FlightDatas{
        val DatasConvertedToArray = rawData.split(";")

        return FlightDatas(
            name = DatasConvertedToArray[0],
            gpsLat = DatasConvertedToArray[1].toFloat(),
            gpsLng = DatasConvertedToArray[2].toFloat(),
            gpsAlt = DatasConvertedToArray[3].toFloat(),
            time = DatasConvertedToArray[4].toInt(),
            temperature = DatasConvertedToArray[5].toFloat(),
            pressure = DatasConvertedToArray[6].toFloat(),
            height = DatasConvertedToArray[7].toFloat(),
            speed = DatasConvertedToArray[8].toFloat(),
            continuity = DatasConvertedToArray[9].toInt(),
            state = DatasConvertedToArray[10].toInt()
        )

    }

    fun resetFlightData() {
        _flightData.value = FlightDatas("", 0f, 0f, 0f, 0, 0f, 0f, 0f, 0f, 0, 0)
    }

}