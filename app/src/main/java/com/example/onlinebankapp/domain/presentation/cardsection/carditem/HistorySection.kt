package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.presentation.history.HistoryComponent
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel

@Composable
fun HistorySection(
    cardId: String,
    operations: List<TransactionData>,
    operationViewModel: OperationViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Operation History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (operations.isEmpty()) {
            Text(
                text = "No Operation Data",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            CardHistoryComponent(
                cardId = cardId,
                operationItemsData = operations,
                operationViewModel = operationViewModel
            )
        }
    }
}