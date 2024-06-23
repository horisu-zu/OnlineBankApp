package com.example.onlinebankapp.domain.presentation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.operation.OperationCardData

@Composable
fun OperationList(
    operations: List<OperationCardData> = getOperationTypeData()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .padding(horizontal = 36.dp)
            .fillMaxWidth()
    ) {
        items(operations) { operation ->
            OperationType(operationCardData = operation)
        }
        item {
            AddCard(onClick = {})
        }
    }
}

@Composable
fun OperationType(
    operationCardData: OperationCardData,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy((-8).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OperationCard(
            operationCardData = operationCardData,
            onClick = {

            }
        )
        Text(
            text = operationCardData.operationName,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OperationCard(
    operationCardData: OperationCardData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(operationCardData.operationColor, Color.White),
        start = Offset(0f, 0f),
        end = Offset(275f, 275f)
    )

    Card(
        modifier = modifier
            .padding(12.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(brush = gradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = operationCardData.operationIcon),
                contentDescription = operationCardData.operationName,
                tint = getTint(operationCardData.operationColor)
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
                    val strokeWidth = 2.dp.toPx()
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
            operationColor = Color.Green,
            operationIcon = R.drawable.ic_market
        ),
        OperationCardData(
            operationName = "Phone",
            operationColor = Color.Red,
            operationIcon = R.drawable.ic_phone
        ),
        OperationCardData(
            operationName = "Transfer",
            operationColor = Color.Yellow,
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
        Color.LightGray
    } else {
        Color.White
    }
}