package com.example.onlinebankapp

import android.app.Application
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.onlinebankapp.di.AppModule
import com.example.onlinebankapp.di.AppModuleImpl
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

class OnlineBankApp: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        appModule = AppModuleImpl(this)
    }
}