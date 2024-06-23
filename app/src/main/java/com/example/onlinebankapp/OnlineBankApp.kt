package com.example.onlinebankapp

import android.app.Application
import com.example.onlinebankapp.di.AppModule
import com.example.onlinebankapp.di.AppModuleImpl
import dagger.hilt.android.HiltAndroidApp

class OnlineBankApp: Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()

        appModule = AppModuleImpl(this)
    }
}