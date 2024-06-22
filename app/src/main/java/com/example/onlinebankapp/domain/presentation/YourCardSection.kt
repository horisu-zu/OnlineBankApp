package com.example.onlinebankapp.domain.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.data.CardType
import com.example.onlinebankapp.data.CurrencyType
import com.example.onlinebankapp.data.PaymentCardData


@Composable
fun YourCardSection(
) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            YourCardsButton(Modifier.padding(start = 48.dp))
            PaymentCardPager(items = getCardData())
        }
    }
}

@Composable
fun YourCardsButton(
    //onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { /* onClick() */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ),
        modifier = modifier
    ) {
        Text(
            text = "Your Cards",
            style = MaterialTheme.typography.titleSmall
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )
    }
}


fun getCardData(): List<PaymentCardData> {
    return listOf(
        PaymentCardData(
            cardName = "Visa Classic",
            cardNumber = "1234 5678 9101 1121",
            expiryMonth = "12",
            expiryYear = "24",
            cvv = "123",
            currentBalance = 1000f,
            currency = CurrencyType.UAH,
            cardType = CardType.VISA,
            cardColor = Color(0XFF7FFFD4)
        ),
        PaymentCardData(
            cardName = "MasterCard Gold",
            cardNumber = "1111 2222 3333 4444",
            expiryMonth = "06",
            expiryYear = "25",
            cvv = "456",
            currentBalance = 2500.12f,
            currency = CurrencyType.EUR,
            cardType = CardType.MASTERCARD,
            cardColor = Color(0xFFE3E934)
        ),
        PaymentCardData(
            cardName = "American Express Black",
            cardNumber = "1111 2222 3333 4444",
            expiryMonth = "09",
            expiryYear = "26",
            cvv = "456",
            currentBalance = 1232.12f,
            currency = CurrencyType.USD,
            cardType = CardType.AMEX,
            cardColor = Color.Black
        )
    )
}