package com.example.minitv.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.minitv.data.model.ReportEntity

@Dao
interface MiniTVDao {

    @Insert
    fun saveReport(tvProgramEntity: ReportEntity)

    @Query("SELECT * FROM reports")
    fun getReports(): List<ReportEntity>
}