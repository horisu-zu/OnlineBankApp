package com.example.onlinebankapp.domain.presentation.template

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OperationButton(
    modifier: Modifier = Modifier,
    label: String = "Confirm Operation",
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color(0xFF33691E) else Color.Transparent,
            contentColor = if (enabled) Color.White else Color.DarkGray,
            disabledContentColor = Color.DarkGray
        ),
        border = if (enabled) BorderStroke(0.dp, Color.Transparent)  else
            BorderStroke(2.dp, Color.DarkGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )
    }
}