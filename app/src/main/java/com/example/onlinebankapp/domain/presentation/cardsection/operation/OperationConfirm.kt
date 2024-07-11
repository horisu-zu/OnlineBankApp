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
import java.util.Date

@Composable
fun OperationConfirm(
    operationData: OperationData,
    sourceCardData: PaymentCardData,
    destinationCardData: PaymentCardData?,
    inputAmount: String,
    onConfirmClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val transactionRepo = TransactionRepositoryImpl(FirebaseFirestore.getInstance())
    val transactionViewModel = remember { TransactionViewModel(transactionRepo)}
    val transactionState by transactionViewModel.addTransactionState.collectAsState()

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

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
            Text(text = "Card Name: ${sourceCardData.cardName}", color = Color.DarkGray)
            Text(text = "Card Number: ${sourceCardData.cardNumber.takeLast(4)
                .padStart(sourceCardData.cardNumber.length, '*')}", color = Color.DarkGray)
            DashedDivider(color = Color.DarkGray)
            Text( text = "Operation Data: ${operationData.title}", color = Color.DarkGray)
            Text( text = "Amount: $inputAmount", color = Color.DarkGray)
            Text( text = "Currency: ${sourceCardData.currency}", color = Color.DarkGray)
            OperationButton(
                onClick = {
                    scope.launch {
                        val transactionData = TransactionData(
                            transactionId = "",
                            operationId = operationData.operationId,
                            operationDate = Date(),
                            sourceCardId = sourceCardData.cardId,
                            destinationCardId = destinationCardData?.cardId ?: operationData.title,
                            amount = inputAmount.toDoubleOrNull() ?: 0.0,
                            currency = sourceCardData.currency,
                            status = TransactionStatus.PENDING,
                            description = operationData.title
                        )
                        transactionViewModel.createTransaction(transactionData)
                    }
                },
                enabled = true
            )
        }
        when (val state = transactionState) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                LaunchedEffect(state) {
                    onConfirmClick()
                }
            }
            is Resource.Error -> {
                LaunchedEffect(state) {
                    showToast = true
                    toastMessage = state.message ?: "Unknown Error"
                }
            }
            null -> {}
        }
    }

    ErrorToast(
        message = toastMessage,
        isVisible = showToast,
        onDismiss = { showToast = false },
        modifier = Modifier.padding(top = 60.dp)
    )
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
