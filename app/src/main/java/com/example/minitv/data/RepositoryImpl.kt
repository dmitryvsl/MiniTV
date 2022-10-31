package com.example.minitv.data

import com.example.minitv.data.database.MiniTVDao
import com.example.minitv.data.model.ReportEntity
import com.example.minitv.data.model.fromDomain
import com.example.minitv.data.model.toDomain
import com.example.minitv.domain.model.Report
import com.example.minitv.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: MiniTVDao
) : Repository {

    override fun saveReport(report: Report) {
        dao.saveReport(report.fromDomain())
    }

    override fun getReports(): List<Report> =
        dao.getReports().map { reportEntity -> reportEntity.toDomain() }
}