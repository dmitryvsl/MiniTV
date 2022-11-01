package com.example.minitv.presentation.main_activity

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.minitv.domain.model.Report
import com.example.minitv.domain.use_cases.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> = _reports

    fun saveReport(report: Report) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.saveReport(report)
        }
    }

    fun getReports() {
        viewModelScope.launch(Dispatchers.IO) {
            val reports = useCase.getReports()
            _reports.postValue(reports)
        }
    }


    companion object {

        fun provideFactory(useCase: UseCase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return MainActivityViewModel(useCase) as T
                }
            }
    }
}