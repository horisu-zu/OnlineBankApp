package com.example.onlinebankapp.domain.presentation.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.presentation.template.ItemDivider
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryComponent(
    backgroundColor: Color,
    operationItems: List<OperationItemData>
) {
    val groupedOperations = groupOperationsByDate(operationItems)

    LazyColumn {
        groupedOperations.forEach { (date, operations) ->
            stickyHeader {
                DateHeader(date)
            }
            itemsIndexed(operations) { index, operation ->
                HistoryItem(operationItemData = operation)
                if (index < operations.size - 1) {
                    ItemDivider(backgroundColor = backgroundColor)
                }
            }
        }
    }
}

@Composable
fun DateHeader(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AnotherGray)
    ) {
        Text(
            text = date,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun HistoryItem(
    operationItemData: OperationItemData
) {
    Row(
        modifier = Modifier
            .background(SlightlyGrey)
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(getSoftColor(operationItemData.iconColor, 0.25f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = operationItemData.operationIcon),
                    contentDescription = operationItemData.operationType,
                    tint = operationItemData.iconColor,
                    modifier = Modifier.size(36.dp)
                )
            }
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = operationItemData.operationType,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = formatTime(operationItemData.operationDate),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Text(
            text = "${isReceivedOperation(operationItemData)}${operationItemData.operationAmount}" +
                    " ${operationItemData.operationCurrency}",
            fontSize = 15.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun getSoftColor(color: Color, alpha: Float = 0.5f): Color {
    return color.copy(alpha = alpha)
}

fun isReceivedOperation(operationItemData: OperationItemData): String {
    return if(operationItemData.isReceived) {
        "+"
    } else {
        "-"
    }
}

fun formatTime(date: Date): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(date)
}

fun formatDate(date: Date): String {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val dateYear = Calendar.getInstance().apply { time = date }.get(Calendar.YEAR)

    val dayOfWeekFormat = SimpleDateFormat("EE", Locale.getDefault())
    val dayMonthFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    val dayOfWeek = dayOfWeekFormat.format(date).take(2).capitalize(Locale.ROOT)
    val dayMonth = dayMonthFormat.format(date)

    return if (dateYear == currentYear) {
        "$dayOfWeek, $dayMonth"
    } else {
        val year = yearFormat.format(date)
        "$dayOfWeek, $dayMonth $year"
    }
}

fun groupOperationsByDate(operations: List<OperationItemData>): Map<String, List<OperationItemData>> {
    return operations
        .sortedByDescending { it.operationDate }
        .groupBy { formatDate(it.operationDate) }
        .toSortedMap(compareByDescending { parseDate(it) })
}

fun parseDate(formattedDate: String): Date {
    val formatter = SimpleDateFormat("EE, d MMMM yyyy", Locale.getDefault())
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    return try {
        formatter.parse(formattedDate)
    } catch (e: ParseException) {
        formatter.parse("$formattedDate $currentYear")
    } ?: throw IllegalArgumentException("Unable to parse date: $formattedDate")
}

/*
fun getMonthNumber(monthName: String): Int {
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    for (i in 0..11) {
        if (monthFormat.format(Date(2020, i, 1)) == monthName) {
            return i
        }
    }
    throw IllegalArgumentException("Unknown Month: $monthName")
}*/
