package com.example.onlinebankapp.domain.presentation.cardsection.operation

import androidx.compose.runtime.Composable
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.presentation.cardsection.operation.input.InnerTransferInput
import com.example.onlinebankapp.domain.presentation.cardsection.operation.input.PaymentInput
import com.example.onlinebankapp.domain.presentation.cardsection.operation.input.TopUpOperationInput
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel

@Composable
fun OperationInput(
    operationData: OperationData,
    cardData: List<PaymentCardData>,
    inputAmount: String,
    initialCardIndex: Int,
    onAmountEntered: (String, PaymentCardData, PaymentCardData?) -> Unit
) {
    when (operationData.operationId) {
        "top_up" -> TopUpOperationInput(
            cardData = cardData,
            initialCardIndex = initialCardIndex,
            inputAmount = inputAmount,
            onAmountEntered = onAmountEntered
        )
        "inner_transfer" -> InnerTransferInput(
            cardData = cardData,
            initialSourceCardIndex = initialCardIndex,
            initialDestinationCardIndex = 0,
            inputAmount = inputAmount,
            onAmountEntered = onAmountEntered
        )
        "steam_payment" -> PaymentInput(
            cardData = cardData,
            label = "Enter Your Login",
            initialCardIndex = 0,
            inputAmount = inputAmount,
            onAmountEntered = onAmountEntered
        )
        else -> {}
        /*is OperationType.Transfer -> TransferInputContent(cardData, viewModel)
        is OperationType.Payment -> PaymentInputContent(cardData, viewModel)*/
    }
}