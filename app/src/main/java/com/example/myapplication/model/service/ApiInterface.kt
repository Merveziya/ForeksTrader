package com.example.myapplication.model.service

import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.StockRequestData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("default/ForeksMobileInterviewSettings")
    suspend fun getStockList(): Response<StockListResponseData>

    @GET("default/ForeksMobileInterview?fields=pdd,las&stcs=GARAN.E.BIST~XU100.I.BIST")
    suspend fun getRequestList(): Response<StockRequestData>
}


