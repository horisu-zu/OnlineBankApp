package com.example.onlinebankapp.domain.presentation.service

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.presentation.history.getSoftColor
import com.example.onlinebankapp.domain.presentation.shimmerEffect
import com.example.onlinebankapp.domain.presentation.template.ItemDivider
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun ServicesListScreen(
    navController: NavController,
    operationViewModel: OperationViewModel
) {
    val operationTypes by operationViewModel.typeState.collectAsState()

    LaunchedEffect(Unit) {
        operationViewModel.getOperations()
    }

    when(operationTypes) {
        is Resource.Loading -> {
            LazyColumn {
                items(10) {
                    ShimmerOperationItem()
                    ItemDivider(backgroundColor = SlightlyGrey)
                }
            }
        }
        is Resource.Success -> {
            operationTypes.data?.let { typesList ->
                LazyColumn {
                    itemsIndexed(typesList) { index, operationType ->
                        OperationTypeItem(
                            operationType = operationType,
                            operationViewModel = operationViewModel,
                            onClick = { typeId ->
                                navController.navigate("operationList/$typeId")
                            }
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
                message = operationTypes.message ?: "Unknown Error",
                onRetry = { operationViewModel.getOperationTypes() }
            )
        }
    }
}

@Composable
fun OperationTypeItem(
    operationType: OperationType,
    operationViewModel: OperationViewModel,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val operationCount by operationViewModel.getOperationsCountForType(operationType.typeId)
        .collectAsState(initial = 0)

    Row(
        modifier = modifier
            .background(SlightlyGrey)
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick(operationType.typeId) },
        horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(getSoftColor(operationType.iconColor, 0.25f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = operationType.icon),
                contentDescription = operationType.title,
                tint = operationType.iconColor,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = operationType.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = Modifier.weight(1f)
        )
        if(operationCount > 1) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "More",
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun ShimmerOperationItem(modifier: Modifier = Modifier) {
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
                .size(64.dp)
                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .height(20.dp)
                .width(150.dp)
                .background(Color.Gray.copy(alpha = 0.2f))
                .shimmerEffect()
        )
    }
}

@Composable
fun ErrorCard(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Error",
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
                Text(
                    text = message,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(text = "Retry", color = Color.White)
                }
            }
        }
    }
}