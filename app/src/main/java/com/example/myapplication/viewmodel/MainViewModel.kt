package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.StockRequestData
import com.example.myapplication.model.service.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiInterface: ApiInterface
) : ViewModel() {
    private val uiStateMutable = MutableStateFlow(StockListResponseData())
    val uiState = uiStateMutable.asLiveData()

    private val uiStateMutablee = MutableStateFlow(StockRequestData())
    val uiStatee = uiStateMutablee.asLiveData()

    init {
        getStockList()
        getRequestList()
    }

    fun getStockList() {
        viewModelScope.launch {
            try {
                val response = apiInterface.getStockList()

                if (response.isSuccessful) {
                    val stockList = response.body()?.mypageDefaults ?: emptyList()
                    uiStateMutable.value = response.body() ?: StockListResponseData(emptyList(), emptyList())

                    stockList.forEach { stock ->
                        Log.d("MainViewModel", "Stock: ${stock.cod}, ${stock.def}, ${stock.gro}, ${stock.tke}")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching stock list", e)
            }
        }
    }

    fun getRequestList() {
        viewModelScope.launch {
            try {
                val response = apiInterface.getRequestList()

                if (response.isSuccessful) {
                    val requestList = response.body()?.fields ?: emptyList()
                    uiStateMutablee.value = response.body() ?: StockRequestData(emptyList())

                    requestList.forEach { attribute ->
                        Log.d(
                            "MainViewModel",
                            "Attribute: ${attribute.tke}, ${attribute.clo}, ${attribute.pdd}, ${attribute.las}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching request list", e)
            }
        }
    }
}
