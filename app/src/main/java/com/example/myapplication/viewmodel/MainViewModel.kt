package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.StockRequestData
import com.example.myapplication.model.service.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
) : ViewModel() {
    private val uiStateMutableResponse = MutableStateFlow(StockListResponseData())
    private val uiStateMutableRequest = MutableStateFlow(StockRequestData())
    private val previousLasValues = mutableListOf<String>()

    private val combinedStateFlow =
        uiStateMutableResponse.combine(uiStateMutableRequest) { stockListResponse, stockRequest ->
            Pair(stockListResponse, stockRequest)
        }

    val uiStateCombined: LiveData<Pair<StockListResponseData, StockRequestData>> =
        combinedStateFlow.asLiveData()


    init {
        viewModelScope.launch {
            val responseValue = getStockList()
            if (responseValue != null) {
                val stcsList = responseValue.mypageDefaults?.map {
                    it.tke
                }
                var codeString = ""
                stcsList?.let { list ->
                    list.forEach {
                        codeString = codeString + it + "~"
                    }
                }
                getRequestList("pdd,las", codeString)
            }
        }
    }

    //Senkronize olması için suspend kullanıldı
    suspend fun getStockList() = withContext(Dispatchers.IO) {
        try {
            val response = apiInterface.getStockList()
            if (response.isSuccessful) {
                val stockListResponseData = response.body() ?: StockListResponseData()
                uiStateMutableResponse.value = stockListResponseData
                return@withContext stockListResponseData
            } else {
                Log.e(
                    "MainViewModel",
                    "Error fetching stock list: ${response.errorBody()?.string()}"
                )
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error fetching stock list", e)
            return@withContext null

        }
    }

    fun getRequestList(fields: String, stcs: String) {
        viewModelScope.launch {
            try {
                val response = apiInterface.getRequestList(fields, stcs)

                if (response.isSuccessful) {
                    val stockRequestData = response.body() ?: StockRequestData()
                    val fieldsValue = stockRequestData?.fields?.firstOrNull()
                    val currentLasValue = fieldsValue?.las

                    stockRequestData.fields?.forEach { fields ->
                        previousLasValues.add(fields.las ?: "0.0")
                    }

                    uiStateMutableRequest.value = stockRequestData

                } else {
                    Log.e(
                        "MainViewModel",
                        "Error fetching request list: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching request list", e)
            }
        }
    }

    fun getPreviousLasValue(position: Int): Double {
           return previousLasValues[position].convertTurkishDouble() ?: 0.0
    }

    fun String.convertTurkishDouble(): Double? { val sanitizedString = this.replace(".", "")
        val formattedString = sanitizedString.replace(",", ".")
        return formattedString.toDoubleOrNull() }
}




