package com.example.onlinebankapp.domain.presentation.cardsection

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.OperationActivity
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.operation.OperationCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.presentation.OperationBottomSheet
import com.example.onlinebankapp.domain.presentation.history.getSoftColor

@Composable
fun OperationList(
    operations: List<OperationData>,
    selectedOperations: List<OperationData>,
    onSelectedOperationsChange: (List<OperationData>) -> Unit,
    onSaveClick: (List<String>) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        items(selectedOperations) { operation ->
            OperationType(operationCardData = operation)
        }
        item {
            AddCard(onClick = { showBottomSheet = true })
        }
    }

    OperationBottomSheet(
        operationsList = operations,
        initialSelectedOperations = selectedOperations,
        showBottomSheet = showBottomSheet,
        onDismissRequest = { showBottomSheet = false },
        onSelectedOperationsChange = onSelectedOperationsChange,
        onSaveClick = { selectedIds ->
            onSaveClick(selectedIds)
            showBottomSheet = false
        }
    )
}

@Composable
fun OperationType(
    operationCardData: OperationData,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy((-8).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OperationCard(
            operationCardData = operationCardData,
            onClick = {
                val intent = Intent(context, OperationActivity::class.java).apply {
                    putExtra("operationDataId", operationCardData.operationId)
                }
                context.startActivity(intent)
            }
        )
        Text(
            text = operationCardData.title,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OperationCard(
    operationCardData: OperationData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(12.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clickable(onClick = onClick)
                //Doesn't working how it's supposed to, but let it be as it is...
                .background(getSoftColor(operationCardData.iconColor, alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = operationCardData.icon),
                contentDescription = operationCardData.title,
                tint = operationCardData.iconColor,
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Composable
fun AddCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(12.dp)
    ) {
        Card(
            modifier = Modifier
                .drawBehind {
                    val strokeWidth = 4.dp.toPx()
                    val dashWidth = 15f
                    val gapWidth = 10f
                    val pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashWidth, gapWidth),
                        0f
                    )
                    drawRoundRect(
                        color = Color.LightGray,
                        size = size,
                        style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                        cornerRadius = CornerRadius(size.width / 2, size.height / 2)
                    )
                }
                .clip(CircleShape)
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        shape = CircleShape
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Operation Type",
                    tint = Color.LightGray
                )
            }
        }
        Text(
            text = "Add",
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun getOperationTypeData(): List<OperationCardData> {
    return listOf(
        OperationCardData(
            operationName = "Envelope",
            operationColor = Color.Blue,
            operationIcon = R.drawable.ic_envelope
        ),
        OperationCardData(
            operationName = "Market",
            operationColor = Color.Yellow,
            operationIcon = R.drawable.ic_market
        ),
        OperationCardData(
            operationName = "Phone",
            operationColor = Color.Red,
            operationIcon = R.drawable.ic_phone
        ),
        OperationCardData(
            operationName = "Transfer",
            operationColor = Color.Green,
            operationIcon = R.drawable.ic_transfer
        ),
        OperationCardData(
            operationName = "Military Aid",
            operationColor = Color.Gray,
            operationIcon = R.drawable.ic_military_aid
        )
    )
}

fun getTint(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() > 0.5) {
        Color.Black
    } else {
        Color.White
    }
}