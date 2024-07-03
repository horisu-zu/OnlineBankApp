package com.example.onlinebankapp.domain.presentation.viewmodel.card

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.addcard.getCardServiceFromNumber
import com.example.onlinebankapp.domain.repository.CardRepository
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardViewModel(private val cardRepository: CardRepository) : ViewModel() {
    private val _cardData = MutableStateFlow(
        PaymentCardData(
            ownerId = "",
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
    private val _addCardState = MutableStateFlow<Resource<Void?>>(Resource.Loading())
    val addCardState: StateFlow<Resource<Void?>> = _addCardState.asStateFlow()

    val cardService = derivedStateOf {
        getCardServiceFromNumber(_cardData.value.cardNumber)
    }

    fun updateCardData(newData: PaymentCardData) {
        _cardData.value = newData
    }

    fun addCard(cardData: PaymentCardData) {
        viewModelScope.launch {
            cardRepository.addCard(cardData).collect { result ->
                _addCardState.value = result
            }
        }
    }
}