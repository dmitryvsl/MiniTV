package com.example.minitv.domain.model

import java.util.Date

data class Report(
    val id: Int,
    val videoId: Int,
    val videoName: String,
    val startTime: Date
)