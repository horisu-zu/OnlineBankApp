package com.example.onlinebankapp.data

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class OperationCardData(
    val operationName: String,
    val operationIcon: Int,
    val operationColor: Color
) {
}