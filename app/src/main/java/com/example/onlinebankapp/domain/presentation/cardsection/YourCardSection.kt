package com.example.onlinebankapp.domain.presentation.cardsection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData

@Composable
fun YourCardSection() {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.Start
        ) {
            YourCardsButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier.padding(start = 24.dp)
            )
            PaymentCardPager(items = getCardData())
        }
    }

    CardBottomSheet(
        items = getCardData(),
        showBottomSheet = showBottomSheet,
        onDismissRequest = { showBottomSheet = false }
    )
}

@Composable
fun YourCardsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ),
        modifier = modifier
            .padding(horizontal = 12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
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
            ownerId = "",
            cardName = "Visa Classic",
            cardNumber = "4149 5678 9101 1121",
            expiryMonth = "12",
            expiryYear = "24",
            cvv = "123",
            currentBalance = 1000f,
            currency = CurrencyType.UAH,
            cardService = CardService.VISA,
            cardType = CardType.CREDIT,
            cardColor = Color(0XFF7FFFD4)
        ),
        PaymentCardData(
            ownerId = "",
            cardName = "MasterCard Gold",
            cardNumber = "5168 2222 3333 4444",
            expiryMonth = "06",
            expiryYear = "25",
            cvv = "456",
            currentBalance = 2500.12f,
            currency = CurrencyType.EUR,
            cardService = CardService.MASTERCARD,
            cardType = CardType.CREDIT,
            cardColor = Color(0xFFE3E934)
        ),
        PaymentCardData(
            ownerId = "",
            cardName = "American Express Black",
            cardNumber = "3742 2222 3333 4182",
            expiryMonth = "09",
            expiryYear = "26",
            cvv = "4567",
            currentBalance = 1232.1224f,
            currency = CurrencyType.USD,
            cardService = CardService.AMEX,
            cardType = CardType.DEBIT,
            cardColor = Color.Black
        ),
        PaymentCardData(
            ownerId = "",
            cardName = "Discover It",
            cardNumber = "6011 1111 2222 3333",
            expiryMonth = "03",
            expiryYear = "27",
            cvv = "789",
            currentBalance = 3500.75f,
            currency = CurrencyType.USD,
            cardService = CardService.DISCOVER,
            cardType = CardType.DEBIT,
            cardColor = Color(0xFFFFA500)
        ),
        PaymentCardData(
            ownerId = "",
            cardName = "UnionPay Platinum",
            cardNumber = "6234 5678 9012 3456",
            expiryMonth = "11",
            expiryYear = "28",
            cvv = "987",
            currentBalance = 20000.50f,
            currency = CurrencyType.CNY,
            cardService = CardService.OTHER,
            cardType = CardType.CREDIT,
            cardColor = Color(0xFFFF4500)
        )
    )
}