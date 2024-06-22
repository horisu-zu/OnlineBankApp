package com.example.onlinebankapp.domain.repository

import com.example.onlinebankapp.data.remote.RetrofitInstance

interface ExchangeRepository {
    suspend fun getExchangeData(from: String, to: String)
}