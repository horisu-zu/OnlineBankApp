package com.example.onlinebankapp.domain.repository

import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.operation.TransactionStatus
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun getTransactionsBy(cardId: String): Flow<Resource<List<TransactionData>>>
    suspend fun createTransaction(transactionData: TransactionData): Flow<Resource<String>>
    suspend fun updateTransaction(status: TransactionStatus, transactionData: TransactionData):
            Flow<Resource<Void?>>
}