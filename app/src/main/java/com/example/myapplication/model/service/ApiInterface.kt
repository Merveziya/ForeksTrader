package com.example.myapplication.model.service

import com.example.myapplication.model.StockListResponseData
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("default/ForeksMobileInterviewSettings")
    suspend fun getStockList(): Response<StockListResponseData>
}
