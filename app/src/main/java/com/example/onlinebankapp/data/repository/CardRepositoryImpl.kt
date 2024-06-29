package com.example.onlinebankapp.data.repository

import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.repository.CardRepository
import com.example.onlinebankapp.domain.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CardRepositoryImpl(private val firestore: FirebaseFirestore): CardRepository {
    override suspend fun getCardInfo(cardId: String): Flow<Resource<PaymentCardData>> = flow {
        emit(Resource.Loading())
        try {
            val documentSnapshot = firestore.collection("paymentCard")
                .document(cardId).get().await()
            val user = documentSnapshot.toObject(PaymentCardData::class.java)
            if (user != null) {
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Card not found"))
            }
        } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun updateCard(cardId: String, cardData: PaymentCardData):
            Flow<Resource<Void?>> = flow {
        emit(Resource.Loading())
        try {
            val result = firestore.collection("paymentCard").document(cardId)
                .set(cardData, SetOptions.merge()).await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun addCard(cardData: PaymentCardData): Flow<Resource<Void?>> = flow {
        emit(Resource.Loading())
        try{
            val result = firestore.collection("paymentCard").document().set(cardData)
                .await()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }
}