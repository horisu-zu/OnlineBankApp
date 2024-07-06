package com.example.onlinebankapp.domain.presentation.template

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.PaymentCardData

@Composable
fun CurrencyCard(
    paymentCardData: PaymentCardData
) {
    Card(
        modifier = Modifier
            .border(2.dp, Color.DarkGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = paymentCardData.currency.toString(),
            color = Color.DarkGray,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}