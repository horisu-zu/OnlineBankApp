package com.example.onlinebankapp.domain.repository

import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface OperationRepository {

    suspend fun getOperationBy(operationId: String): Flow<Resource<OperationData>>
    suspend fun getOperations(): Flow<Resource<List<OperationData>>>
    suspend fun getOperationsByType(typeId: String): Flow<Resource<List<OperationData>>>
    suspend fun getOperationTypes(): Flow<Resource<List<OperationType>>>
}