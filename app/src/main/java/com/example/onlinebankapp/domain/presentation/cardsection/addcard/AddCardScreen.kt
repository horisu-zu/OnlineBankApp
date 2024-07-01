package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.ui.theme.SlightlyGrey


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(viewModel: CardViewModel) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 3
    val cardData by viewModel.cardData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { StepIndicator(
                    stepsCount = totalSteps,
                    currentStep = currentStep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) },
                navigationIcon = {
                    IconButton(onClick = { /*onBackPressed*/ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (currentStep < totalSteps) currentStep++
                        },
                        enabled = currentStep < totalSteps
                    ) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Next")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SlightlyGrey
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentStep) {
                1 -> AddCardComponent(viewModel)
                /*2 -> CheckCardComponent()
                3 -> ConfirmationComponent()*/
            }
        }
    }
}
@Composable
fun AddCardComponent(viewModel: CardViewModel) {
    val cardData by viewModel.cardData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardPreview(cardData = cardData)
        CardInput(
            cardData = cardData,
            onCardDataChange = { viewModel.updateCardData(it) }
        )
    }
}