package com.example.onlinebankapp.data.repository

import com.example.onlinebankapp.data.remote.ExchangeApi
import com.example.onlinebankapp.domain.exchange.ExchangeData
import com.example.onlinebankapp.domain.repository.ExchangeRepository
import com.example.onlinebankapp.domain.util.Resource

class ExchangeRepositoryImpl(
    private val api: ExchangeApi
) : ExchangeRepository {
    override suspend fun getExchangeData(first: String): Resource<ExchangeData> {
        return try {
            val response = api.getExchangeData(first)
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception: ${e.message}")
        }
    }
}