package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Composable
fun CardItemInfo(
    paymentCardData: PaymentCardData,
    operations: List<OperationItemData>,
    modifier: Modifier = Modifier
) {
    CardAppBar(
        paymentCardData = paymentCardData
    ) {
        HistorySection(
            cardId = paymentCardData.cardName,
            operations = operations,
            modifier = modifier
                .fillMaxSize()
                .background(SlightlyGrey)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CardItemInfoPreview() {
    val sampleCardData = PaymentCardData(
        ownerId = "",
        cardName = "John Doe",
        cardNumber = "1234 5678 9012 3456",
        expiryMonth = "12",
        expiryYear = "25",
        cvv = "721",
        cardColor = Color(0xFF1E88E5),
        currentBalance = 1234.56f,
        currency = CurrencyType.USD,
        cardService = CardService.VISA,
        cardType = CardType.DEBIT
    )

    val localDate = LocalDate.of(2023, 6, 28)
    val operationDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

    val sampleOperations = listOf(
        OperationItemData(
            cardId = "John Doe",
            operationType = "Credit",
            operationIcon = R.drawable.ic_money,
            iconColor = Color.Green,
            operationDate = operationDate,
            operationAmount = 100.12,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            cardId = "John Doe",
            operationType = "Market",
            operationIcon = R.drawable.ic_market,
            iconColor = Color.Blue,
            operationDate = Date(),
            operationAmount = 50.124,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        )
    )

    OnlineBankAppTheme {
        Surface {
            CardItemInfo(
                paymentCardData = sampleCardData,
                operations = sampleOperations
            )
        }
    }
}
