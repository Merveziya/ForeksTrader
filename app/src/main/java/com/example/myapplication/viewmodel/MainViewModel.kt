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
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        getRequestList("pdd,las", "GARAN.E.BIST~XU100.I.BIST")
    }

    fun getStockList() {
        viewModelScope.launch {
            try {
                val response = apiInterface.getStockList()
                if (response.isSuccessful) {
                    val stockListResponseData = response.body() ?: StockListResponseData()
                    uiStateMutable.value = stockListResponseData

                    stockListResponseData.mypageDefaults?.forEach { StockListDetail ->
                        Log.d("MainViewModel", "Stock: ${StockListDetail.cod}, ${StockListDetail.gro}, ${StockListDetail.tke},  ${StockListDetail.def}")
                    }
                } else {
                    Log.e("MainViewModel", "Error fetching stock list: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching stock list", e)
            }
        }
    }

    fun getRequestList(fields: String, stcs: String) {
        viewModelScope.launch {
            try {
                val response = apiInterface.getRequestList(fields, stcs)
                if (response.isSuccessful) {
                    val stockRequestData = response.body() ?: StockRequestData()
                    uiStateMutablee.value = stockRequestData

                    stockRequestData.fields?.forEach { attribute ->
                        Log.d(
                            "MainViewModel",
                            "Attribute: ${attribute.tke}, ${attribute.clo}, ${attribute.pdd}, ${attribute.las}"
                        )
                    }
                } else {
                    Log.e("MainViewModel", "Error fetching request list: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching request list", e)
            }
        }
    }
}
