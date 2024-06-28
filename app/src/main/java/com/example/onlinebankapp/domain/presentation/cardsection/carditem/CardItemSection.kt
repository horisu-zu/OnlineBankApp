package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import java.util.Date

@Composable
fun CardItemSection(
    paymentCardData: PaymentCardData,
    operations: List<OperationItemData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CardHeader(
            paymentCardData = paymentCardData
        )
        HistorySection(
            cardId = paymentCardData.cardName,
            operations = operations
        )
    }
}

@Composable
fun CardItemSectionPreview() {
    val sampleCardData = PaymentCardData(
        cardName = "John Doe",
        cardNumber = "1234 5678 9012 3456",
        expiryMonth = "12",
        expiryYear = "25",
        cvv = "721",
        cardColor = Color(0xFF1E88E5),
        currentBalance = 1234.56f,
        currency = CurrencyType.USD,
        cardType = CardType.VISA
    )

    val sampleOperations = listOf(
        OperationItemData(
            cardId = "John Doe",
            operationType = "Market",
            operationIcon = R.drawable.ic_market,
            iconColor = Color.Blue,
            operationDate = Date(),
            operationAmount = 50.124,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        ),
        OperationItemData(
            cardId = "John Doe",
            operationType = "Credit",
            operationIcon = R.drawable.ic_money,
            iconColor = Color.Green,
            operationDate = Date(System.currentTimeMillis() - 86400000),
            operationAmount = 100.12,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        )
    )

    // Используем тему вашего приложения
    OnlineBankAppTheme {
        Surface {
            CardItemSection(
                paymentCardData = sampleCardData,
                operations = sampleOperations
            )
        }
    }
}
