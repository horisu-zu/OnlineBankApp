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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.data.repository.TransactionRepositoryImpl
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.operation.TransactionStatus
import com.example.onlinebankapp.domain.presentation.auth.ErrorToast
import com.example.onlinebankapp.domain.presentation.template.OperationButton
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.TransactionViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OperationConfirm(
    operationData: OperationData,
    sourceCardData: PaymentCardData,
    destinationCardData: PaymentCardData?,
    inputAmount: String,
    onConfirmClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AnotherGray)
    ) {
        TransactionComponent(
            sourceCardData = sourceCardData,
            destinationCardData = destinationCardData,
            operationData = operationData,
            inputAmount = inputAmount,
            onConfirmClick = onConfirmClick
        )
    }
}

@Composable
private fun TransactionComponent(
    sourceCardData: PaymentCardData,
    destinationCardData: PaymentCardData?,
    operationData: OperationData,
    inputAmount: String,
    onConfirmClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AnotherGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Source Card", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text("Card Name: ${sourceCardData.cardName}", color = Color.DarkGray)
            Text("Card Number: •••• ${sourceCardData.cardNumber.takeLast(4)}",
                color = Color.DarkGray)
            Text("Currency: ${sourceCardData.currency}", color = Color.DarkGray)

            DashedDivider(color = Color.DarkGray)

            if (destinationCardData != null) {
                Text("Destination Card", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text("Card Name: ${destinationCardData.cardName}", color = Color.DarkGray)
                Text("Card Number: •••• ${destinationCardData.cardNumber.takeLast(4)}",
                    color = Color.DarkGray)
                Text("Currency: ${destinationCardData.currency}", color = Color.DarkGray)
            } else {
                Text("Destination: ${operationData.title}", color = Color.DarkGray)
            }

            DashedDivider(color = Color.DarkGray)

            Text("Operation Data", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text("Operation: ${operationData.title}", color = Color.DarkGray)
            Text("Amount: $inputAmount", color = Color.DarkGray)
            Text("Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault()).format(Date())}", color = Color.DarkGray)

            OperationButton(
                onClick = onConfirmClick,
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
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
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
