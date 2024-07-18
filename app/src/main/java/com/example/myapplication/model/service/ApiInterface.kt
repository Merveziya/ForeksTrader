package com.example.myapplication.model.service

import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.StockResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("default/ForeksMobileInterviewSettings")
    suspend fun getStockList(): Response<StockListResponseData>


    @GET("ForeksMobileInterview")
    suspend fun getRequestList(
        @Query("fields") fields: String,
        @Query("stcs") stcs: String
    ): Response<StockResponseData>
}
