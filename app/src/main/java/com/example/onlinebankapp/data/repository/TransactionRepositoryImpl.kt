package com.example.onlinebankapp.data.repository

import android.util.Log
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.operation.TransactionStatus
import com.example.onlinebankapp.domain.repository.TransactionRepository
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TransactionRepositoryImpl(private val firestore: FirebaseFirestore): TransactionRepository {
    private val transactionsCollection = firestore.collection("transaction")
    private val cardCollection = firestore.collection("paymentCard")

    override suspend fun getTransactionsBy(userId: String): Flow<Resource<List<TransactionData>>> = flow {
        emit(Resource.Loading())
        try {
            val userCards = cardCollection
                .whereEqualTo("ownerId", userId)
                .get()
                .await()
                .toObjects(PaymentCardData::class.java)

            Log.d("TransactionRepository", userCards.size.toString())

            val cardIds = userCards.map { it.cardId }

            val sourceTransactions = transactionsCollection
                .whereIn("sourceCardId", cardIds)
                .get()
                .await()

            val destinationTransactions = transactionsCollection
                .whereIn("destinationCardId", cardIds)
                .get()
                .await()

            val transactions = (sourceTransactions.toObjects(TransactionData::class.java) +
                    destinationTransactions.toObjects(TransactionData::class.java))
                .distinctBy{ it.transactionId }

            Log.d("TransactionRepository", "Number of transactions: ${transactions.size}")

            emit(Resource.Success(transactions))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }

    /*override suspend fun getTransactionsFor(cardId: String): Flow<Resource<List<TransactionData>>> = flow {
        emit(Resource.Loading())
        try {
            val sourceTransactionsSnapshot = transactionsCollection
                .whereEqualTo("sourceCardId", cardId)
                .orderBy("operationDate", Query.Direction.DESCENDING)
                .get()
                .await()

            val destinationTransactionsSnapshot = transactionsCollection
                .whereEqualTo("destinationCardId", cardId)
                .orderBy("operationDate", Query.Direction.DESCENDING)
                .get()
                .await()

            val sourceTransactions = sourceTransactionsSnapshot.documents.mapNotNull { doc ->
                doc.toObject(TransactionData::class.java)
            }

            val destinationTransactions = destinationTransactionsSnapshot.documents.mapNotNull { doc ->
                doc.toObject(TransactionData::class.java)
            }

            val allTransactions = (sourceTransactions + destinationTransactions).sortedByDescending { it.operationDate }

            emit(Resource.Success(allTransactions))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }*/

    override suspend fun getTransactionsFor(cardId: String): Flow<Resource<List<TransactionData>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = transactionsCollection
                .where(
                    Filter.or(
                        Filter.equalTo("sourceCardId", cardId),
                        Filter.equalTo("destinationCardId", cardId)
                    )
                )
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