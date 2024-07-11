package com.example.onlinebankapp.data.repository

import android.util.Log
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.operation.TransactionStatus
import com.example.onlinebankapp.domain.repository.TransactionRepository
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TransactionRepositoryImpl(private val firestore: FirebaseFirestore): TransactionRepository {
    private val transactionsCollection = firestore.collection("transaction")

    override suspend fun getTransactionsBy(cardId: String): Flow<Resource<List<TransactionData>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = transactionsCollection
                .whereEqualTo("sourceCardId", cardId)
                .orderBy("operationDate", Query.Direction.DESCENDING)
                .get()
                .await()

            val transactions = snapshot.documents.mapNotNull { doc ->
                doc.toObject(TransactionData::class.java)
            }
            emit(Resource.Success(transactions))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    override suspend fun createTransaction(transactionData: TransactionData): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            Log.d("CreateTransaction", "Attempting to create transaction")
            val docRef = transactionsCollection.document()
            val transaction = transactionData.copy(transactionId = docRef.id)
            docRef.set(transaction).await()
            Log.d("CreateTransaction", "Transaction created successfully")
            emit(Resource.Success(docRef.id))
        } catch (e: Exception) {
            Log.e("CreateTransaction", "Error creating transaction", e)
            emit(Resource.Error(e.message ?: "Failed to create transaction"))
        }
    }

    override suspend fun updateTransaction(status: TransactionStatus, transactionData: TransactionData): Flow<Resource<Void?>> = flow {
        emit(Resource.Loading())
        try {
            val updatedData = transactionData.copy(status = status)
            val result = transactionsCollection.document(transactionData.transactionId)
                .set(updatedData)
                .await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update transaction"))
        }
    }
}