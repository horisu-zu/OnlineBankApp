package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.presentation.history.DateHeader
import com.example.onlinebankapp.domain.presentation.history.HistoryItem
import com.example.onlinebankapp.domain.presentation.history.formatDate
import com.example.onlinebankapp.domain.presentation.history.groupOperationsByDate
import com.example.onlinebankapp.domain.presentation.history.parseDate
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun CardHistoryComponent(
    cardId: String,
    operationItemsData: List<TransactionData>,
    operationViewModel: OperationViewModel
) {
    val operationState by operationViewModel.operationState.collectAsState()

    when (val state = operationState) {
        is Resource.Success -> {
            val operations = state.data
            val groupedOperations = groupOperationsByDate(operationItemsData)

            groupedOperations.forEach { (date, transactions) ->
                DateHeader(date = date)
                transactions.forEachIndexed { index, transactionData ->
                    val operationData = operations?.find { it.operationId == transactionData.operationId }
                    HistoryItem(
                        cardId = cardId,
                        transactionData = transactionData,
                        operationData = operationData
                    )
                    if (index < transactions.size - 1) {
                        Divider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier
                                .background(SlightlyGrey)
                                .padding(start = 80.dp, end = 18.dp)
                                .alpha(0.5f)
                        )
                    }
                }
            }
        }
        is Resource.Loading -> {
        }
        is Resource.Error -> {
        }
    }
}