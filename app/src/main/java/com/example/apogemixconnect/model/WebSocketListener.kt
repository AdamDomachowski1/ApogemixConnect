package com.example.apogemixconnect.model

import android.util.Log
import com.example.apogemixconnect.viewmodel.WebSocketViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener(
    private val viewModel: WebSocketViewModel
): WebSocketListener() {

    private val TAG = "SOCKET"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(true)
        webSocket.send("Android Device Connected")
        Log.d(TAG, "onOpen:")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        viewModel.updateData(text)
        viewModel.handleIncomingMessage(Pair(false, text))
        Log.d(TAG, "onMessage: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(false)
        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }

    suspend fun changeFrequency(freqMHz: Int): Boolean {
        val client = OkHttpClient()
        val url = "http://192.168.4.1/?freqmhz=$freqMHz"
        val request = Request.Builder().url(url).build()

        return client.newCall(request).execute().use { response ->
            response.isSuccessful
        }
    }
}