package com.example.onlinebankapp.domain.repository

import com.example.onlinebankapp.data.remote.RetrofitInstance
import com.example.onlinebankapp.domain.exchange.ExchangeData
import com.example.onlinebankapp.domain.util.Resource

interface ExchangeRepository {
    suspend fun getExchangeData(first: String): Resource<ExchangeData>
}