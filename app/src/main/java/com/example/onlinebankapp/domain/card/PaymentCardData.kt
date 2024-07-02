package com.example.onlinebankapp.domain.card

import androidx.compose.ui.graphics.Color

data class PaymentCardData(
    var ownerId: String,
    val cardName: String,
    val cardNumber: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cvv: String,
    val currentBalance: Float,
    val currency: CurrencyType,
    val cardService: CardService,
    val cardType: CardType,
    val cardColor: Color
)

enum class CurrencyType {
    EUR, USD, UAH, GBP, CNY, JPY
}

enum class CardType {
    CREDIT, DEBIT
}

enum class CardService {
    VISA, MASTERCARD, AMEX, DISCOVER, OTHER
}