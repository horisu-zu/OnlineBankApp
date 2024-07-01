package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.getCardLogo
import com.example.onlinebankapp.domain.presentation.cardsection.getTextColorForBackground

@Composable
fun CardPreview(
    modifier: Modifier = Modifier,
    cardData: PaymentCardData
) {
    val gradient = Brush.linearGradient(
        colors = listOf(cardData.cardColor, Color.White),
        start = Offset(0f, 0f),
        end = Offset(750f, 750f)
    )
    val textColor = getTextColorForBackground(cardData.cardColor)

    val cardService = getCardTypeFromNumber(cardData.cardNumber)
    val cardName = generateCardName(cardData.cardService, cardData.cardType == CardType.DEBIT)

    Card(
        modifier = modifier.padding(24.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(brush = gradient)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = cardName,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = formatCardNumber(cardData.cardNumber),
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatExpiryDate(cardData.expiryMonth, cardData.expiryYear),
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            Image(
                painter = painterResource(id = getCardLogo(cardService)),
                contentDescription = cardData.cardService.toString(),
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

fun getCardTypeFromNumber(number: String): CardService {
    val cleanNumber = number.replace("\\D".toRegex(), "")
    return when {
        cleanNumber.startsWith("3") -> CardService.AMEX
        cleanNumber.startsWith("4") -> CardService.VISA
        cleanNumber.startsWith("5") || cleanNumber.startsWith("2") ->
            CardService.MASTERCARD
        cleanNumber.startsWith("6") -> CardService.DISCOVER
        else -> CardService.OTHER
    }
}

fun generateCardName(cardService: CardService, isDebit: Boolean): String {
    val cardBrand = when (cardService) {
        CardService.VISA -> "Visa"
        CardService.MASTERCARD -> "Mastercard"
        CardService.AMEX -> "American Express"
        CardService.DISCOVER -> "Discover"
        CardService.OTHER -> "Other"
    }
    val cardCategory = if (isDebit) "Debit" else "Credit"
    return "$cardBrand $cardCategory Card"
}

fun formatExpiryDate(month: String, year: String): String {
    val formattedMonth = month.padStart(2, '0').take(2)
    val formattedYear = year.padStart(2, '0').takeLast(2)
    return if (month.isEmpty() && year.isEmpty()) "MM/YY" else "$formattedMonth/$formattedYear"
}

fun formatCardNumber(cardNumber: String): String {
    val formatted = cardNumber.replace("\\s".toRegex(), "").padEnd(16, '*')
    return formatted.chunked(4).joinToString(" ")
}