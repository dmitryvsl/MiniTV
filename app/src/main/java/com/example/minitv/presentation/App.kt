package com.example.minitv.presentation

import android.app.Application
import com.example.minitv.di.AppComponent
import com.example.minitv.di.DaggerAppComponent

class App : Application() {

    companion object {
        val component: AppComponent by lazy(LazyThreadSafetyMode.NONE) {
            DaggerAppComponent.create()
        }
    }


}