package com.example.onlinebankapp.domain.presentation.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun ItemDivider(
    backgroundColor: Color
) {
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier
            .background(backgroundColor)
            .padding(start = 80.dp, end = 18.dp)
            .alpha(0.5f)
    )
}