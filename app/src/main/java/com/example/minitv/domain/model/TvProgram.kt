package com.example.minitv.domain.model

import com.google.gson.annotations.SerializedName

data class TvProgram(
    @SerializedName("VideoId")
    val videoId: Int,
    @SerializedName("VideoIdentifier")
    val videoName: String,
    @SerializedName("OrderNumber")
    val orderNumber: Int
)