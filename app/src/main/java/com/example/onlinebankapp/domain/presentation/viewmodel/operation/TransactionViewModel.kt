package com.example.onlinebankapp.domain.presentation.viewmodel.operation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.operation.TransactionStatus
import com.example.onlinebankapp.domain.repository.TransactionRepository
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {

    private val _transactionData = MutableStateFlow<Resource<List<TransactionData>>>(Resource.Loading())
    val transactionData: StateFlow<Resource<List<TransactionData>>> = _transactionData.asStateFlow()

    private val _addTransactionState = MutableStateFlow<Resource<String>?>(null)
    val addTransactionState = _addTransactionState.asStateFlow()

    /*fun getTransactionsBy(userId: String) {
        viewModelScope.launch {
            try {
                transactionRepository.getTransactionsFor()
            } catch (e: Exception) {
                _transactionData.emit(Resource.Error
                    ("Failed to fetch transactions: ${e.message}"))
            }
        }
    }*/

    fun getTransactionsFor(cardId: String) {
        viewModelScope.launch {
            try {
                transactionRepository.getTransactionsFor(cardId).collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            Log.e("TransactionViewModel",
                                "Error fetching transactions: ${result.message}")
                        }
                        is Resource.Loading -> {
                            Log.d("TransactionViewModel",
                                "Loading transactions for cardId: $cardId")
                        }
                        is Resource.Success -> _transactionData.value = result
                    }
                }
            } catch (e: Exception) {
                _transactionData.emit(Resource.Error
                    ("Failed to fetch transactions: ${e.message}"))
            }
        }
    }

    fun createTransaction(transactionData: TransactionData) {
        viewModelScope.launch {
            try {
                transactionRepository.createTransaction(transactionData).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _addTransactionState.emit(result)
                        }
                        is Resource.Error -> {
                            Log.e("CreateTransaction", "Error: ${result.message}")
                            _addTransactionState.emit(result)
                        }
                        is Resource.Loading -> {
                            Log.d("CreateTransaction", "Creating Transaction...")
                            _addTransactionState.emit(result)
                        }
                    }
                }
            } catch (e: Exception) {
                _addTransactionState.emit(Resource.Error
                    ("Failed to create transaction: ${e.message}"))
            }
        }
    }

    fun updateTransactionStatus(status: TransactionStatus, transactionData: TransactionData) {
        viewModelScope.launch {
            try {
                transactionRepository.updateTransaction(status, transactionData).collect { result ->
                    when (result) {
                        is Resource.Success -> _addTransactionState.emit(Resource.Success
                            (transactionData.transactionId))
                        is Resource.Error -> _addTransactionState.emit(Resource.Error
                            (result.message ?: "Failed to update transaction"))
                        is Resource.Loading -> _addTransactionState.emit(Resource.Loading())
                    }
                }
            } catch (e: Exception) {
                _addTransactionState.emit(Resource.Error
                    ("Failed to update transaction: ${e.message}"))
            }
        }
    }
}