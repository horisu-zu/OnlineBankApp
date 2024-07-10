package com.example.onlinebankapp.domain.presentation.cardsection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.template.toColor
import java.util.Locale

@Composable
fun SmallPaymentCard(
    cardData: PaymentCardData,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(cardData.cardColor.toColor(), Color.White),
        start = Offset(0f, 0f),
        end = Offset(200f, 200f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        Card(
            modifier = Modifier
                .size(width = 120.dp, height = 64.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.elevatedCardElevation(1.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = getCardLogo(cardData.cardService)),
                    contentDescription = cardData.cardType.toString(),
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        Column {
            Text(
                text = "•••• ${cardData.cardNumber.takeLast(4)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            val formattedBalance = "%.2f".format(Locale.ENGLISH, cardData.currentBalance)
            Text(
                text = "$formattedBalance ${cardData.currency}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}