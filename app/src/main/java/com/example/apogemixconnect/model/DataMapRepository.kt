package com.example.apogemixconnect.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object DataMapRepository {
    private val _dataMap = MutableLiveData<Map<String, String>>(mapOf())
    val dataMap: LiveData<Map<String, String>> = _dataMap

    @Synchronized
    fun updateDataMap(key: String, value: String) {
        val updatedMap = _dataMap.value.orEmpty().toMutableMap()
        updatedMap[key] = value
        _dataMap.postValue(updatedMap)
    }

    @Synchronized
    fun resetDataMap() {
        _dataMap.postValue(mapOf())
    }
}
