package com.example.onlinebankapp.di

import android.content.Context
import com.example.onlinebankapp.data.remote.ExchangeApi
import com.example.onlinebankapp.data.repository.ExchangeRepositoryImpl
import com.example.onlinebankapp.domain.repository.ExchangeRepository
import com.example.onlinebankapp.domain.util.Defaults
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppModule {
    val exchangeApi: ExchangeApi
    val exchangeRepository: ExchangeRepository
}

class AppModuleImpl(
    private val appContext: Context
): AppModule {
    override val exchangeApi: ExchangeApi by lazy {
        Retrofit.Builder()
            .baseUrl(Defaults.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
    override val exchangeRepository: ExchangeRepository by lazy {
        ExchangeRepositoryImpl(exchangeApi)
    }
}
