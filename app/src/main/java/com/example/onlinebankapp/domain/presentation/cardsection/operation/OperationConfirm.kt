package com.example.onlinebankapp.domain.presentation.cardsection.operation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.presentation.template.OperationButton
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.ui.theme.AnotherGray

@Composable
fun OperationConfirm(
    operationData: OperationData,
    cardViewModel: CardViewModel,
    inputAmount: String,
    onConfirmClick: () -> Unit
) {
    val cardData by cardViewModel.cardData.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AnotherGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Card Name: ${cardData.cardName}", color = Color.DarkGray)
            Text(text = "Card Number: ${cardData.cardNumber.takeLast(4)
                .padStart(cardData.cardNumber.length, '*')}", color = Color.DarkGray)
            DashedDivider()
            Text( text = "Operation Data: ${operationData.title}", color = Color.DarkGray)
            Text( text = "Amount: $inputAmount", color = Color.DarkGray)
            Text( text = "Currency: ${cardData.currency}", color = Color.DarkGray)
            OperationButton(
                onClick = { onConfirmClick() },
                enabled = true
            )
        }
    }
}

@Composable
fun DashedDivider(
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 10.dp,
    gapLength: Dp = 5.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.height(thickness)) {
        val canvasWidth = size.width
        val dashPx = dashLength.toPx()
        val gapPx = gapLength.toPx()
        var currentX = 0f

        while (currentX < canvasWidth) {
            drawLine(
                color = color,
                start = Offset(x = currentX, y = 0f),
                end = Offset(x = currentX + dashPx, y = 0f),
                strokeWidth = thickness.toPx()
            )
            currentX += dashPx + gapPx
        }
    }
}
