package com.example.onlinebankapp.domain.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.exchange.ExchangeData
import com.example.onlinebankapp.domain.repository.ExchangeRepository
import com.example.onlinebankapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangeViewModel(
    private val repository: ExchangeRepository
) : ViewModel() {

    private val conversionRates: MutableMap<String,
            MutableLiveData<Resource<ExchangeData>>> = mutableMapOf()

    fun fetchConversionRate(currencyName: String) {
        viewModelScope.launch {
            try {
                val result = repository.getExchangeData(currencyName)
                getConversionRateLiveData(currencyName).value = result
            } catch (e: Exception) {
                getConversionRateLiveData(currencyName).value =
                    Resource.Error(message = e.message ?: "Unknown error")
            }
        }
    }

    fun getConversionRateLiveData(currencyName: String): MutableLiveData<Resource<ExchangeData>> {
        if (!conversionRates.containsKey(currencyName)) {
            conversionRates[currencyName] = MutableLiveData()
        }
        return conversionRates[currencyName]!!
    }
}