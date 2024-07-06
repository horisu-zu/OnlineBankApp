package com.example.onlinebankapp.domain.presentation.template

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepAppBar(
    currentStep: Int,
    totalSteps: Int,
    onBackPressed: () -> Unit,
    onNextPressed: () -> Unit,
    isNextEnabled: Boolean,
    isBackEnabled: Boolean = true,
    title: String = ""
) {
    TopAppBar(
        title = {
            StepIndicator(
                stepsCount = totalSteps,
                currentStep = currentStep,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackPressed,
                enabled = isBackEnabled
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (isBackEnabled) Color.DarkGray else Color.LightGray
                )
            }
        },
        actions = {
            IconButton(
                onClick = onNextPressed,
                enabled = isNextEnabled
            ) {
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = if (isNextEnabled) Color.DarkGray else Color.LightGray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SlightlyGrey
        )
    )
}