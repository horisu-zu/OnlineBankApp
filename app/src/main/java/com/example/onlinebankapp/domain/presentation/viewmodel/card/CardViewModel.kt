package com.example.onlinebankapp.domain.presentation.viewmodel.card

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CardViewModel(private val cardRepository: CardRepository) : ViewModel() {
    private val _cardData = MutableStateFlow(
        PaymentCardData(
            cardName = "",
            cardNumber = "",
            expiryMonth = "",
            expiryYear = "",
            cvv = "",
            currentBalance = 0f,
            currency = CurrencyType.UAH,
            cardService = CardService.OTHER,
            cardType = CardType.CREDIT,
            cardColor = Color.Black
        )
    )
    val cardData: StateFlow<PaymentCardData> = _cardData.asStateFlow()

    fun updateCardData(newData: PaymentCardData) {
        _cardData.value = newData
    }

    suspend fun saveCard() {
        cardRepository.addCard(_cardData.value)
    }
}