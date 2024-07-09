package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class StockListDetail(
     @SerializedName("cod") val cod: String? = null,
     @SerializedName("gro") val gro: String? = null,
     @SerializedName("tke") val tke: String? = null,
     @SerializedName("def") val def: String? = null
)

data class Column(
     @SerializedName("name") val name: String? = null,
     @SerializedName("key") val key: String? = null
)

data class StockListResponseData(
     @SerializedName("mypageDefaults") val mypageDefaults: List<StockListDetail>? = null,
     @SerializedName("mypage") val myPage: List<Column>? = null,
)
