package com.example.apogemixconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.apogemixconnect.model.DataMapRepository
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class DataMapRepositoryTest {

    // Reguła InstantTaskExecutorRule zapewnia, że operacje związane z LiveData
    // są wykonywane synchronicznie i natychmiastowo w testach.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Test sprawdza funkcję updateDataMap w DataMapRepository.
    @Test
    fun updateDataMap_updatesValue() {
        val key = "testKey" // Klucz do zapisania w mapie
        val value = "testValue" // Wartość do zapisania w mapie

        // Wywołanie metody updateDataMap, aby zaktualizować mapę danych.
        DataMapRepository.updateDataMap(key, value)

        // Pobranie aktualnej wartości mapy danych z LiveData.
        val dataMapValue = DataMapRepository.dataMap.value

        // Sprawdzenie, czy mapa danych zawiera klucz 'testKey'.
        assertTrue(dataMapValue?.containsKey(key) == true)

        // Sprawdzenie, czy wartość skojarzona z kluczem 'testKey' jest równa 'testValue'.
        assertEquals(value, dataMapValue?.get(key))
    }

    // Test sprawdza funkcję resetDataMap w DataMapRepository.
    @Test
    fun resetDataMap_clearsData() {
        // Najpierw aktualizujemy mapę danych, aby mieć pewność, że zawiera jakieś dane.
        DataMapRepository.updateDataMap("key1", "value1")

        // Następnie resetujemy mapę danych.
        DataMapRepository.resetDataMap()

        // Pobranie aktualnej wartości mapy danych z LiveData po jej zresetowaniu.
        val dataMapValue = DataMapRepository.dataMap.value

        // Sprawdzenie, czy mapa danych jest pusta po zresetowaniu.
        assertTrue(dataMapValue.isNullOrEmpty())
    }
}
