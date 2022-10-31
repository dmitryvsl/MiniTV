package com.example.minitv.di

import com.example.minitv.domain.use_cases.GetReports
import com.example.minitv.domain.use_cases.SaveReport
import com.example.minitv.domain.use_cases.UseCase
import com.example.minitv.presentation.utils.GsonUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGsonUtils(): GsonUtils = GsonUtils()

    @Provides
    @Singleton
    fun provideUseCase(
        getReports: GetReports,
        saveReport: SaveReport
    ): UseCase = UseCase(getReports, saveReport)
}