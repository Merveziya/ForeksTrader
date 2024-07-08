package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.StockListResponseData
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

    init {
        getStockList()
    }

    fun getStockList() {
        viewModelScope.launch {
            try {
                val response = apiInterface.getStockList()
                if (response.isSuccessful) {
                    val stockList = response.body()?.mypageDefaults
                    val firstStock = stockList?.firstOrNull()
                    uiStateMutable.value = StockListResponseData(
                        mypageDefaults = stockList,
                        converterText = firstStock?.cod
                    )
                    Log.d("MainViewModel", "Message: ${firstStock?.cod}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error!!")
            }
        }
    }
}
