package com.example.myapplication.model.service

import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.StockRequestData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("default/ForeksMobileInterviewSettings")
    suspend fun getStockList(): Response<StockListResponseData>

    /*
    @GET("ForeksMobileInterview?fields=las,pdd,ddi,low,hig,buy,sel,pdc,cei,flo,gco&stcs=XU100.I.BIST~XU050.I.BIST~XU030.I.BIST~USD/TRL~EUR/TRL~EUR/USD~XAU/USD~XGLD~BRENT")
    suspend fun getRequestList(): Response<StockRequestData>
    */

    @GET("ForeksMobileInterview")
    suspend fun getRequestList(
        @Query("fields") fields: String,
        @Query("stcs") stcs: String
    ): Response<StockRequestData>
}
