package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.presentation.history.DateHeader
import com.example.onlinebankapp.domain.presentation.history.HistoryItem
import com.example.onlinebankapp.domain.presentation.history.groupOperationsByDate
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun CardHistoryComponent(
    operationItemsData: List<OperationItemData>
) {
    val groupedOperations = groupOperationsByDate(operationItemsData)

    groupedOperations.forEach { (date, operations) ->
        DateHeader(date = date)
        operations.forEachIndexed { index, operationItemData ->
            HistoryItem(operationItemData = operationItemData)
            if(index < operations.size - 1) {
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