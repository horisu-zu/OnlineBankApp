package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun CardItemInfo(
    paymentCardData: PaymentCardData,
    operations: List<TransactionData>,
    operationViewModel: OperationViewModel,
    modifier: Modifier = Modifier
) {
    CardAppBar(
        paymentCardData = paymentCardData
    ) {
        HistorySection(
            cardId = paymentCardData.cardId,
            operations = operations,
            operationViewModel = operationViewModel,
            modifier = modifier
                .fillMaxSize()
                .background(SlightlyGrey)
        )
    }
}
