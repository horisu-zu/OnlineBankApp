package com.example.onlinebankapp.domain.presentation.cardsection.operation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.PaymentCardData

@Composable
fun OperationResult(
    cardData: PaymentCardData,
    destinationCardData: PaymentCardData?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color.Green,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "Operation Successful",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
        Text(
            text = "Card Name: ${cardData.cardName}",
            color = Color.DarkGray
        )
        Text(
            text = "New Balance: ${cardData.currentBalance} ${cardData.currency}",
            color = Color.DarkGray
        )
    }
}