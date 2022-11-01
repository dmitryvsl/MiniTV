package com.example.minitv.di

import com.example.minitv.presentation.main_activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RepositoryModule::class])
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

}