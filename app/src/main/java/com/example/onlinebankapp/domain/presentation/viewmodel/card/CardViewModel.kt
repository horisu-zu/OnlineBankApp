package com.example.onlinebankapp.domain.presentation.viewmodel.card

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.addcard.getCardServiceFromNumber
import com.example.onlinebankapp.domain.repository.CardRepository
import com.example.onlinebankapp.domain.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(private val cardRepository: CardRepository) : ViewModel() {
    private val _cardData = MutableStateFlow(
        PaymentCardData(
            cardId = "",
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
            cardColor = "0xFFFFFFF"
        )
    )
    val cardData: StateFlow<PaymentCardData> = _cardData.asStateFlow()
    private val _addCardState = MutableStateFlow<Resource<String>>(Resource.Loading())
    val addCardState: StateFlow<Resource<String>> = _addCardState.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val userCards: StateFlow<Resource<List<PaymentCardData>>> = _userId
        .filterNotNull()
        .flatMapLatest { userId ->
            cardRepository.getCardsBy(userId)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Loading())

    fun setUserId(userId: String) {
        _userId.value = userId
    }

    fun getCardById(cardId: String) {
        viewModelScope.launch {
            cardRepository.getCardInfo(cardId).collect { result ->
                when (result) {
                    is Resource.Success -> _cardData.value = result.data!!
                    is Resource.Error -> Log.e("CardViewModel",
                        "Error loading card: ${result.message}")
                    is Resource.Loading -> Log.d("CardViewModel", "Loading card...")
                }
            }
        }
    }

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
                when (result) {
                    is Resource.Success -> {
                        val newCardData = cardData.copy(cardId = result.data.toString())
                        _cardData.value = newCardData
                    }
                    is Resource.Error -> Log.e("CardViewModel",
                        "Error adding card: ${result.message}")
                    is Resource.Loading -> Log.d("CardViewModel", "Adding card...")
                }
            }
        }
    }
}