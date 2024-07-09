package com.example.onlinebankapp.domain.presentation.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.presentation.history.getSoftColor
import com.example.onlinebankapp.domain.presentation.template.ItemDivider
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun OperationListScreen(
    typeId: String,
    operationViewModel: OperationViewModel
) {
    val operations by operationViewModel.operationState.collectAsState()

    LaunchedEffect(Unit) {
        operationViewModel.getOperations(typeId)
    }

    when(operations) {
        is Resource.Loading -> {
            LazyColumn {
                items(3) {
                    ShimmerOperationItem()
                    ItemDivider(backgroundColor = SlightlyGrey)
                }
            }
        }
        is Resource.Success -> {
            operations.data?.let { typesList ->
                LazyColumn {
                    itemsIndexed(typesList) { index, operationData ->
                        OperationItem(
                            operationData = operationData
                        )
                        if (index < typesList.size - 1) {
                            ItemDivider(backgroundColor = SlightlyGrey)
                        }
                    }
                }
            }
        }
        is Resource.Error -> {
            ErrorCard(
                message = operations.message ?: "Unknown Error",
                onRetry = { operationViewModel.getOperationTypes() }
            )
        }
    }
}

@Composable
fun OperationItem(
    operationData: OperationData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(SlightlyGrey)
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(getSoftColor(operationData.iconColor, 0.25f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = operationData.icon),
                contentDescription = operationData.title,
                tint = operationData.iconColor,
                modifier = Modifier.size(36.dp)
            )
        }
        Text(
            text = operationData.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier.weight(1f)
        )
    }
}
