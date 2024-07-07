package com.example.onlinebankapp.domain.card

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.onlinebankapp.R
import java.util.Date

data class PaymentCardData(
    val cardId: String = "",
    var ownerId: String,
    var cardName: String,
    val cardNumber: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cvv: String,
    val currentBalance: Float,
    val currency: CurrencyType,
    val cardService: CardService,
    val cardType: CardType,
    val cardColor: String,
    val createdAt: Date = Date()
) {
    constructor() : this(
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
}

enum class CurrencyType {
    EUR, USD, UAH, GBP, CNY, JPY
}

enum class CardType {
    CREDIT, DEBIT
}

enum class CardService {
    VISA, MASTERCARD, AMEX, DISCOVER, OTHER
}
