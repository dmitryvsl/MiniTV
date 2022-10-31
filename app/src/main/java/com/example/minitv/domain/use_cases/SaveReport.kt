package com.example.minitv.domain.use_cases

import com.example.minitv.domain.model.Report
import com.example.minitv.domain.repository.Repository
import javax.inject.Inject

class SaveReport @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(report: Report){
        repository.saveReport(report)
    }
}