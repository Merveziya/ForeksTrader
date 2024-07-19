package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.MainResponseData
import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.StockResponseData
import com.example.myapplication.model.service.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
) : ViewModel() {
    private val stockListMutableResponse = MutableStateFlow(StockListResponseData())
    private var mainMutableResponse =
        MutableStateFlow(MainResponseData(StockResponseData(), StockResponseData()))

    private val combinedStateFlow =
        stockListMutableResponse.combine(mainMutableResponse) { stockListResponse, mainResponseData ->
            Pair(stockListResponse, mainResponseData)
        }

    val combinedLiveData: LiveData<Pair<StockListResponseData, MainResponseData>> =
        combinedStateFlow.asLiveData()

    private var jobCoroutines:Job ?= null

    private var firstColumnSortType:SortColumnValue ?= null


    //Senkronize olması için suspend kullanıldı
    suspend fun getStockList() = withContext(Dispatchers.IO) {
        try {
            val response = apiInterface.getStockList()
            if (response.isSuccessful) {
                val stockListResponseData = response.body() ?: StockListResponseData()
                stockListMutableResponse.value = stockListResponseData
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

    fun getResponseList(fields: String, stcs: String) {
        viewModelScope.launch {
            try {
                val response = apiInterface.getRequestList(fields, stcs)

                if (response.isSuccessful) {
                    val stockResponseData = response.body() ?: StockResponseData()

                    mainMutableResponse.value.oldStocklistResponseData =
                        mainMutableResponse.value.currentResponseData
                    val oldData = mainMutableResponse.value.oldStocklistResponseData
                    mainMutableResponse.value = MainResponseData(stockResponseData, oldData)
                    Log.e("lala","lala")


                    /*
                    stockResponseData.fields?.let { fields ->
                        when(SortColumnValue.sortOrder()) {
                            SortColumnValue.ASCENDING -> fields.sortedBy {
                                it.las
                            }
                            SortColumnValue.DESCENDING -> fields.sortedByDescending {
                                it.las
                            }
                        }
                    }
*/
                } else {
                    Log.e(
                        "MainViewModel",
                        "Error fetching request listttt: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching request list", e)
            }
        }
    }


    fun onActivityStopped(){
        jobCoroutines?.cancel()
    }

    fun updateResponseData(coroutineScope: CoroutineScope, timeRange: Long) {
        jobCoroutines = coroutineScope.launch {
            while (isActive) {
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
                    getResponseList("pdd,las", codeString)
                }
                delay(timeRange)
            }

        }
    }
}

fun String.convertTurkishDouble(): Double? { val sanitizedString = this.replace(".", "")
    val formattedString = sanitizedString.replace(",", ".")
    return formattedString.toDoubleOrNull()
}


enum class SortColumnValue{
    ASCENDING,DESCENDING;
    companion object{
        fun sortOrder(column: String){

        }
    }
}


