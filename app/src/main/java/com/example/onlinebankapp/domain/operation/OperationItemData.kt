package com.example.onlinebankapp.domain.operation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.getCardData
import java.util.Calendar
import java.util.Date

data class OperationData(
    val operationId: String,
    val title: String,
    @DrawableRes val icon: Int,
    val iconColor: Color,
    val operationTypeId: String?
)

data class OperationType(
    val typeId: String,
    val title: String,
    @DrawableRes val icon: Int, // R.drawable.nameICon
    val iconColor: Color, //Color.Black
)

data class TransactionData(
    val transactionId: String,
    val operationTypeId: String,
    val operationDataId: String,
    val operationDate: Date,
    val sourceCardId: String,
    val destinationCardId: String,
    val amount: Double,
    val currency: CurrencyType,
    val status: String,
    val description: String?
)

object OperationTypes {
    val TOP_UP = OperationType(
        typeId = "TOP_UP",
        title = "Top Up",
        icon = R.drawable.ic_credit,
        iconColor = Color(0xFF4CAF50)
    )

    val TRANSFER = OperationType(
        typeId = "TRANSFER",
        title = "Transfer",
        icon = R.drawable.ic_transfer,
        iconColor = Color(0xFF2196F3)
    )

    val PAYMENT = OperationType(
        typeId = "PAYMENT",
        title = "Payment",
        icon = R.drawable.ic_payment,
        iconColor = Color(0xFFFFC107)
    )
}

object SampleOperations {
    val operations = listOf(
        OperationData(
            operationId = "top_up",
            title = "Card Top Up",
            icon = R.drawable.ic_credit,
            iconColor = Color(0xFF4CAF50),
            operationTypeId = OperationTypes.TOP_UP.typeId
        ),

        OperationData(
            operationId = "card_transfer",
            title = "Card Transfer",
            icon = R.drawable.ic_payment,
            iconColor = Color(0xFF2196F3),
            operationTypeId = OperationTypes.TRANSFER.typeId
        ),
        OperationData(
            operationId = "phone_transfer",
            title = "Phone Transfer",
            icon = R.drawable.ic_phone_transfer,
            iconColor = Color(0xFFFFC107),
            operationTypeId = OperationTypes.TRANSFER.typeId
        ),

        OperationData(
            operationId = "mobile_payment",
            title = "Mobile Payment",
            icon = R.drawable.ic_phone,
            iconColor = Color(0xFFFFC107),
            operationTypeId = OperationTypes.PAYMENT.typeId
        ),
        OperationData(
            operationId = "payment_2",
            title = "Internet Payment",
            icon = R.drawable.ic_internet,
            iconColor = Color(0xFFFFC107),
            operationTypeId = OperationTypes.PAYMENT.typeId
        )
    )

    fun getOperationsByType(typeId: String): List<OperationData> {
        return operations.filter { it.operationTypeId == typeId }
    }
}

data class OperationItemData(
    val operationType: String,
    val operationDate: Date,
    val cardId: String,
    val operationIcon: Int,
    val iconColor: Color,
    val operationAmount: Double,
    val operationCurrency: CurrencyType,
    val isReceived: Boolean
) {
}

fun operationDataList(): List<OperationItemData> {
    val calendar = Calendar.getInstance()

    return listOf(
        OperationItemData(
            operationType = "Deposit",
            operationDate = calendar.apply { set(2024, Calendar.JUNE, 1,
                10, 15) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_deposit,
            iconColor = Color.Green,
            operationAmount = 100.0,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Payment",
            operationDate = calendar.apply { set(2024, Calendar.MAY, 28,
                9, 30) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_payment,
            iconColor = Color.Magenta,
            operationAmount = 45.0,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Transfer",
            operationDate = calendar.apply { set(2024, Calendar.MAY, 20,
                14, 45) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_transfer,
            iconColor = Color.Blue,
            operationAmount = 150.0,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Withdrawal",
            operationDate = calendar.apply { set(2024, Calendar.MAY, 15,
                11, 20) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_withdrawal,
            iconColor = Color.Red,
            operationAmount = 80.0,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Deposit",
            operationDate = calendar.apply { set(2024, Calendar.MAY, 10,
                16, 0) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_deposit,
            iconColor = Color.Green,
            operationAmount = 200.0,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Payment",
            operationDate = calendar.apply { set(2024, Calendar.MAY,
                5, 13, 25) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_payment,
            iconColor = Color.Magenta,
            operationAmount = 65.0,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Transfer",
            operationDate = calendar.apply { set(2024, Calendar.APRIL,
                30, 10, 50) }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_transfer,
            iconColor = Color.Blue,
            operationAmount = 120.0,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Deposit",
            operationDate = calendar.apply {
                set(2024, Calendar.JUNE, 1, 10, 15)
            }.time,
            cardId = getCardData().get(1).cardName,
            operationIcon = R.drawable.ic_deposit,
            iconColor = Color.Green,
            operationAmount = 100.0,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Withdrawal",
            operationDate = calendar.apply {
                set(2024, Calendar.MAY, 15, 14, 30)
            }.time,
            cardId = getCardData().get(2).cardName,
            operationIcon = R.drawable.ic_withdrawal,
            iconColor = Color.Red,
            operationAmount = 50.0,
            operationCurrency = CurrencyType.EUR,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Transfer",
            operationDate = calendar.apply {
                set(2024, Calendar.MARCH, 10, 12, 39)
            }.time,
            cardId = getCardData().get(2).cardName,
            operationIcon = R.drawable.ic_transfer,
            iconColor = Color.Blue,
            operationAmount = 200.0,
            operationCurrency = CurrencyType.UAH,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Payment",
            operationDate = calendar.apply {
                set(2024, Calendar.MARCH, 10, 16, 45)
            }.time,
            cardId = getCardData().get(3).cardName,
            operationIcon = R.drawable.ic_payment,
            iconColor = Color.Magenta,
            operationAmount = 75.0,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Deposit",
            operationDate = calendar.apply {
                set(2024, Calendar.MARCH, 10, 11, 1)
            }.time,
            cardId = getCardData().get(4).cardName,
            operationIcon = R.drawable.ic_deposit,
            iconColor = Color.Green,
            operationAmount = 300.0,
            operationCurrency = CurrencyType.EUR,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Withdrawal",
            operationDate = calendar.apply {
                set(2024, Calendar.JANUARY, 1, 13, 15)
            }.time,
            cardId = getCardData().get(3).cardName,
            operationIcon = R.drawable.ic_withdrawal,
            iconColor = Color.Red,
            operationAmount = 150.0,
            operationCurrency = CurrencyType.CNY,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Transfer",
            operationDate = calendar.apply {
                set(2023, Calendar.DECEMBER, 25, 8, 45)
            }.time,
            cardId = getCardData().get(0).cardName,
            operationIcon = R.drawable.ic_transfer,
            iconColor = Color.Blue,
            operationAmount = 400.0,
            operationCurrency = CurrencyType.USD,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Payment",
            operationDate = calendar.apply {
                set(2023, Calendar.NOVEMBER, 15, 10, 0)
            }.time,
            cardId = getCardData().get(2).cardName,
            operationIcon = R.drawable.ic_payment,
            iconColor = Color.Magenta,
            operationAmount = 120.0,
            operationCurrency = CurrencyType.EUR,
            isReceived = false
        ),
        OperationItemData(
            operationType = "Deposit",
            operationDate = calendar.apply {
                set(2023, Calendar.OCTOBER, 10, 12, 30)
            }.time,
            cardId = getCardData().get(3).cardName,
            operationIcon = R.drawable.ic_deposit,
            iconColor = Color.Green,
            operationAmount = 250.0,
            operationCurrency = CurrencyType.JPY,
            isReceived = true
        ),
        OperationItemData(
            operationType = "Withdrawal",
            operationDate = calendar.apply {
                set(2023, Calendar.SEPTEMBER, 5, 15, 45)
            }.time,
            cardId = getCardData().get(4).cardName,
            operationIcon = R.drawable.ic_withdrawal,
            iconColor = Color.Red,
            operationAmount = 90.0,
            operationCurrency = CurrencyType.USD,
            isReceived = false
        )
    )
}
