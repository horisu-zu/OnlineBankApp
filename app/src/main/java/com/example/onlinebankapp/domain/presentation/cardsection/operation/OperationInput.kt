package com.example.onlinebankapp.domain.presentation.cardsection.operation

import androidx.compose.runtime.Composable
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.presentation.cardsection.operation.input.TopUpOperationInput
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel

@Composable
fun OperationInput(
    operationData: OperationData,
    cardData: PaymentCardData,
    inputAmount: String,
    onAmountEntered: (String) -> Unit
) {
    when (operationData.operationId) {
        "top_up" -> TopUpOperationInput(cardData, inputAmount, onAmountEntered)
        else -> {}
        /*is OperationType.Transfer -> TransferInputContent(cardData, viewModel)
        is OperationType.Payment -> PaymentInputContent(cardData, viewModel)*/
    }
}