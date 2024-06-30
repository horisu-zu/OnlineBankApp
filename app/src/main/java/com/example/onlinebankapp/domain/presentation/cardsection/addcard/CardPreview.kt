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
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.presentation.cardsection.getCardLogo
import com.example.onlinebankapp.domain.presentation.cardsection.getTextColorForBackground

@Composable
fun CardPreview(
    modifier: Modifier = Modifier,
    cardNumber: String,
    isDebit: Boolean,
    expiryMonth: String,
    expiryYear: String,
    cardColor: Color = Color.White
) {
    val gradient = Brush.linearGradient(
        colors = listOf(cardColor, cardColor.copy(alpha = 0.5f)),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    val textColor = getTextColorForBackground(cardColor)

    val cardType = getCardTypeFromNumber(cardNumber)
    val cardName = generateCardName(cardType, isDebit)

    Card(
        modifier = modifier
            .padding(24.dp),
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
                    text = formatCardNumber(cardNumber),
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatExpiryDate(expiryMonth, expiryYear),
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            Image(
                painter = painterResource(id = getCardLogo(cardType)),
                contentDescription = cardType.toString(),
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

fun getCardTypeFromNumber(number: String): CardType {
    val cleanNumber = number.replace("\\D".toRegex(), "")
    return when {
        cleanNumber.startsWith("3") -> CardType.AMEX
        cleanNumber.startsWith("4") -> CardType.VISA
        cleanNumber.startsWith("5") || cleanNumber.startsWith("2") ->
            CardType.MASTERCARD
        cleanNumber.startsWith("6") -> CardType.DISCOVER
        else -> CardType.OTHER
    }
}

fun generateCardName(cardType: CardType, isDebit: Boolean): String {
    val cardBrand = when (cardType) {
        CardType.VISA -> "Visa"
        CardType.MASTERCARD -> "Mastercard"
        CardType.AMEX -> "American Express"
        CardType.DISCOVER -> "Discover"
        CardType.OTHER -> "Other"
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

@Preview(showBackground = true)
@Composable
fun CardPreviewPreview() {
    val cardTypes = CardType.entries.toTypedArray()
    val isDebitOptions = listOf(true, false)

    Column {
        cardTypes.forEach { cardType ->
            isDebitOptions.forEach { isDebit ->
                CardPreview(
                    cardNumber = when (cardType) {
                        CardType.VISA -> "4111111111111111"
                        CardType.MASTERCARD -> "5555555555554444"
                        CardType.AMEX -> "371449635398431"
                        CardType.DISCOVER -> "6011111111111117"
                        CardType.OTHER -> "9999888877776666"
                    },
                    isDebit = isDebit,
                    expiryMonth = "12",
                    expiryYear = "25",
                    cardColor = when (cardType) {
                        CardType.VISA -> Color(0xFF58F3CF)
                        CardType.MASTERCARD -> Color(0xFFFF5F00)
                        CardType.AMEX -> Color(0xFF2E77BB)
                        CardType.DISCOVER -> Color(0xFFFF6600)
                        CardType.OTHER -> Color.Gray
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}