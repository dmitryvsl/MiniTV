package com.example.minitv.domain.use_cases

import com.example.minitv.domain.repository.Repository
import javax.inject.Inject

class GetReports @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke() = repository.getReports()
}