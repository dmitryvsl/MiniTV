package com.example.minitv.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.minitv.domain.model.Report
import com.example.minitv.domain.model.TvProgram
import java.util.Calendar
import java.util.Date

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "id_video")
    val videoId: Int,

    @ColumnInfo(name = "video_name")
    val videoName: String,

    @ColumnInfo(name = "startTime")
    val startTime: Date
)

fun Report.fromDomain() =
    ReportEntity(
        id = 0,
        videoId = this.videoId,
        videoName = this.videoName,
        startTime = this.startTime
    )

fun ReportEntity.toDomain() =
    Report(
        id = this.id,
        videoId = this.videoId,
        videoName = this.videoName,
        startTime = this.startTime
    )