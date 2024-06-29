package com.example.onlinebankapp.domain.repository

import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CardRepository {

    suspend fun getCardInfo(cardId: String) : Flow<Resource<PaymentCardData>>
    suspend fun updateCard(cardId: String, cardData: PaymentCardData) : Flow<Resource<Void?>>
    suspend fun addCard(cardData: PaymentCardData) : Flow<Resource<Void?>>
}