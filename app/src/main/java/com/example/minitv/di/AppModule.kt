package com.example.minitv.di

import com.example.minitv.presentation.utils.GsonUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGsonUtils(): GsonUtils = GsonUtils()
}