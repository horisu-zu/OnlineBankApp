package com.example.onlinebankapp.data.repository

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.presentation.template.toColor
import com.example.onlinebankapp.domain.presentation.template.toDrawableRes
import com.example.onlinebankapp.domain.repository.OperationRepository
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OperationRepositoryImpl(
    private val firestore: FirebaseFirestore
): OperationRepository {
    override suspend fun getOperationBy(operationId: String): Flow<Resource<OperationData>> = flow {
        emit(Resource.Loading())
        try {
            val documentSnapshot = firestore.collection("operations")
                .document(operationId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val operationData = documentSnapshot.toObject(OperationData::class.java)
                operationData?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Operation data is null"))
            } else {
                emit(Resource.Error("Operation not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override suspend fun getOperations(): Flow<Resource<List<OperationData>>> = flow {
        emit(Resource.Loading())
        try {
            val querySnapshot = firestore.collection("operation")
                .get()
                .await()

            val operations = querySnapshot.documents.mapNotNull { documentSnapshot ->
                val data = documentSnapshot.data
                data?.let {
                    OperationData(
                        operationId = data["operationId"] as String,
                        title = data["title"] as String,
                        icon = (data["icon"] as String).toDrawableRes(),
                        iconColor = (data["iconColor"] as String).toColor(),
                        operationTypeId = data["operationTypeId"] as String
                    )
                }
            }
            emit(Resource.Success(operations))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
            Log.e("Error", e.message.toString())
        }
    }

    override suspend fun getOperationsByType(typeId: String): Flow<Resource<List<OperationData>>> = flow {
        emit(Resource.Loading())
        try {
            val querySnapshot = firestore.collection("operation")
                .whereEqualTo("operationTypeId", typeId)
                .get()
                .await()

            val operations = querySnapshot.documents.mapNotNull { documentSnapshot ->
                val data = documentSnapshot.data
                data?.let {
                    OperationData(
                        operationId = data["operationId"] as String,
                        title = data["title"] as String,
                        icon = (data["icon"] as String).toDrawableRes(),
                        iconColor = (data["iconColor"] as String).toColor(),
                        operationTypeId = data["operationTypeId"] as String
                    )
                }
            }
            emit(Resource.Success(operations))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun getOperationTypes(): Flow<Resource<List<OperationType>>> = flow {
        emit(Resource.Loading())
        try {
            Log.d("OperationRepo", "Starting to fetch operation types")
            val querySnapshot = firestore.collection("operationType").get().await()
            Log.d("OperationRepo",
                "Query snapshot received. Document count: ${querySnapshot.size()}")

            val operations = querySnapshot.documents.mapNotNull { documentSnapshot ->
                val data = documentSnapshot.data
                data?.let {
                    Log.d("OperationRepo", "Processing document: ${documentSnapshot.id}")
                    OperationType(
                        typeId = data["typeId"] as? String ?: "",
                        title = data["title"] as? String ?: "",
                        icon = (data["icon"] as? String)?.toDrawableRes() ?: 0,
                        iconColor = (data["iconColor"] as? String)?.toColor() ?: Color.Black
                    )
                }
            }
            emit(Resource.Success(operations))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}