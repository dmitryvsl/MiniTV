package com.example.minitv.domain.repository

import com.example.minitv.domain.model.Report
import com.example.minitv.domain.model.TvProgram

interface Repository {

    fun saveReport(report: Report)

    fun getReports(): List<Report>
}