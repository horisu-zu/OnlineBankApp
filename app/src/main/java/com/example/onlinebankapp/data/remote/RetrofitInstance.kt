package com.example.onlinebankapp.data.remote

import com.example.onlinebankapp.domain.util.Defaults
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class RetrofitInstance {

    companion object{
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(logging).build()

            Retrofit.Builder().baseUrl(Defaults.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create()).client(client).build()
        }

        val apiCurrency by lazy {
            retrofit.create(ExchangeApi::class.java)
        }
    }
}