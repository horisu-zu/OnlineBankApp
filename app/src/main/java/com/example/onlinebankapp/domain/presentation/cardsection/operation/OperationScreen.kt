package com.example.onlinebankapp.domain.presentation.cardsection.operation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.presentation.template.CurrencyCard
import com.example.onlinebankapp.domain.presentation.template.InputField
import com.example.onlinebankapp.domain.presentation.template.OperationButton
import com.example.onlinebankapp.domain.presentation.template.StepAppBar
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.launch

@Composable
fun OperationScreen(
    operationData: OperationData,
    onBackPressed: () -> Unit,
    viewModel: CardViewModel,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var inputAmount by remember { mutableStateOf("") }

    val totalSteps = 3
    val scope = rememberCoroutineScope()
    val cardData by viewModel.cardData.collectAsState()

    Log.e("SCREEN", operationData.operationId)

    Scaffold(
        topBar = {
            StepAppBar(
                currentStep = currentStep,
                totalSteps = totalSteps,
                onBackPressed = {
                    when (currentStep) {
                        1 -> onBackPressed()
                        else -> currentStep--
                    }
                },
                onNextPressed = {
                    if (currentStep < totalSteps) {
                        if (currentStep == 2) {
                            scope.launch {
                                viewModel.updateCardData(cardData)
                            }
                        }
                        currentStep++
                    }
                    else onBackPressed()
                },
                isNextEnabled = when (currentStep) {
                    1, 3 -> false
                    else -> true
                },
                isBackEnabled = currentStep < totalSteps
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(SlightlyGrey)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AnotherGray
                )
            ) {
                when (currentStep) {
                    1 -> OperationInput(operationData, cardData, inputAmount) { newAmount ->
                        inputAmount = newAmount
                        currentStep++
                    }
                    2 -> OperationConfirm(operationData, viewModel, inputAmount) {
                        currentStep++
                    }
                    3 -> OperationResult(cardData) {
                        scope.launch {
                            viewModel.updateCardData(cardData)
                        }
                    }
                }
            }
        }
    }
}