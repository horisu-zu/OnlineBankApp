package com.example.onlinebankapp.data.remote

import com.example.onlinebankapp.domain.exchange.ExchangeData
import com.example.onlinebankapp.domain.util.Defaults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeApi {
    @GET("v6/${Defaults.API_KEY}/pair/{fromCurrency}/UAH")
    suspend fun getExchangeData(
        @Path("fromCurrency") fromCurrency: String
    ): Response<ExchangeData>
}