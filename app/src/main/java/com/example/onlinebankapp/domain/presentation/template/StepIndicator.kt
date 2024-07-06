package com.example.onlinebankapp.domain.presentation.template

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIndicator(
    stepsCount: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (step in 1..stepsCount) {
            StepItem(
                step = step,
                isCompleted = step < currentStep,
                isCurrent = step == currentStep
            )
            if(step < stepsCount) {
                StepDivider(
                    isCompleted = step < currentStep,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun StepItem(
    step: Int,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    val color = when {
        isCompleted || isCurrent -> Color.DarkGray
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .border(2.dp, color, CircleShape)
            .background(if (isCompleted) color else Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if(isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = Color.White
            )
        } else {
            Text(
                text = step.toString(),
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StepDivider(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if(isCompleted) 1f else 0f,
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing),
        label = "Completed Step Animation"
    )

    Box(
        modifier = modifier
            .height(1.dp)
            .clipToBounds()
    ) {
        Divider(
            color = Color.LightGray,
            modifier = Modifier.fillMaxSize()
        )

        Divider(
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
        )
    }
}