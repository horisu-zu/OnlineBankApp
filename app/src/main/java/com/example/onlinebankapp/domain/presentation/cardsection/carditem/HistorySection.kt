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
import com.example.onlinebankapp.domain.presentation.history.HistoryComponent

@Composable
fun HistorySection(
    cardId: String,
    operations: List<OperationItemData>,
    modifier: Modifier = Modifier
) {
    val cardOperations = operations.filter { it.cardId == cardId }

    Column(modifier = modifier) {
        Text(
            text = "Operation History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (cardOperations.isEmpty()) {
            Text(
                text = "No Operation Data",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            HistoryComponent(operationItems = cardOperations)
        }
    }
}