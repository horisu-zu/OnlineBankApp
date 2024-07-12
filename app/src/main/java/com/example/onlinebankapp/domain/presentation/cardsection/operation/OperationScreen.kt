package com.example.onlinebankapp.domain.presentation.cardsection.operation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.TransactionData
import com.example.onlinebankapp.domain.operation.TransactionStatus
import com.example.onlinebankapp.domain.presentation.auth.ErrorToast
import com.example.onlinebankapp.domain.presentation.template.StepAppBar
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.TransactionViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun OperationScreen(
    operationData: OperationData,
    onBackPressed: () -> Unit,
    viewModel: CardViewModel,
    transactionViewModel: TransactionViewModel,
    userCards: List<PaymentCardData>,
    initialCardIndex: Int,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var inputAmount by remember { mutableStateOf("") }

    val totalSteps = 3
    val scope = rememberCoroutineScope()
    var sourceCardData by remember { mutableStateOf<PaymentCardData?>(null) }
    var destinationCardData by remember { mutableStateOf<PaymentCardData?>(null) }

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    val transactionState by transactionViewModel.addTransactionState.collectAsState()

    ErrorToast(
        message = toastMessage,
        isVisible = showToast,
        onDismiss = { showToast = false }
    )

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
                        currentStep++
                    }
                    else onBackPressed()
                },
                isNextEnabled = when (currentStep) {
                    3 -> true
                    else -> false
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
                    1 -> OperationInput(
                        operationData,
                        userCards,
                        inputAmount,
                        initialCardIndex
                    ) { newAmount, selectedSourceCard, selectedDestinationCard ->
                        inputAmount = newAmount
                        sourceCardData = selectedSourceCard
                        destinationCardData = selectedDestinationCard
                        val result = processOperation(
                            operationData,
                            selectedSourceCard!!,
                            selectedDestinationCard!!,
                            newAmount.toFloatOrNull() ?: 0f
                        )
                        if (result.isSuccess) {
                            val (newSourceCard, newDestinationCard) = result.getOrNull()!!
                            sourceCardData = newSourceCard
                            destinationCardData = newDestinationCard
                            currentStep++
                        } else {
                            toastMessage = result.exceptionOrNull()?.message.toString()
                            showToast = true
                        }
                    }
                    2 -> OperationConfirm(
                        operationData = operationData,
                        sourceCardData = sourceCardData!!,
                        destinationCardData = destinationCardData,
                        inputAmount = inputAmount,
                        onConfirmClick = {
                            scope.launch {
                                val transactionData = TransactionData(
                                    transactionId = "",
                                    operationId = operationData.operationId,
                                    operationDate = Date(),
                                    sourceCardId = sourceCardData!!.cardId,
                                    destinationCardId = destinationCardData?.cardId ?: operationData.title,
                                    amount = inputAmount.toDoubleOrNull() ?: 0.0,
                                    currency = sourceCardData!!.currency,
                                    status = TransactionStatus.PENDING,
                                    description = operationData.title
                                )
                                transactionViewModel.createTransaction(transactionData)
                            }
                        }
                    )
                    3 -> OperationResult(sourceCardData!!, destinationCardData)
                }
            }
        }
        ErrorToast(
            message = toastMessage,
            isVisible = showToast,
            onDismiss = { showToast = false },
            modifier = Modifier.padding(top = 60.dp)
        )
    }

    LaunchedEffect(transactionState) {
        when (val state = transactionState) {
            is Resource.Success -> {
                viewModel.updateCardData(sourceCardData!!)
                destinationCardData?.let { viewModel.updateCardData(it) }
                currentStep++
            }
            is Resource.Error -> {
                toastMessage = state.message ?: "Unknown Error"
                showToast = true
            }
            is Resource.Loading -> {}
            else -> {}
        }
    }
}

private fun processOperation(
    operationData: OperationData,
    sourceCard: PaymentCardData,
    destinationCard: PaymentCardData,
    amount: Float
): Result<Pair<PaymentCardData, PaymentCardData?>> {
    return when (operationData.operationTypeId) {
        "TOP_UP" -> {
            val newBalance = destinationCard.currentBalance + amount
            Result.success(Pair(sourceCard.copy(currentBalance = newBalance), null))
        }
        "PAYMENT" -> {
            if (amount > sourceCard.currentBalance) {
                Result.failure(Exception("Insufficient funds"))
            } else {
                val newBalance = sourceCard.currentBalance - amount
                Result.success(Pair(sourceCard.copy(currentBalance = newBalance), null))
            }
        }
        "TRANSFER" -> {
            if (destinationCard == sourceCard) {
                Result.failure(Exception("Destination card is missing"))
            } else if (amount > sourceCard.currentBalance) {
                Result.failure(Exception("Insufficient funds"))
            } else if(destinationCard.currency != sourceCard.currency) {
                Result.failure(Exception("Transfer between different currencies currently isn't working"))
            } else {
                val newSourceBalance = sourceCard.currentBalance - amount
                val newDestinationBalance = destinationCard.currentBalance + amount
                Result.success(
                    Pair(
                        sourceCard.copy(currentBalance = newSourceBalance),
                        destinationCard.copy(currentBalance = newDestinationBalance)
                    )
                )
            }
        }
        else -> Result.failure(Exception("Unknown operation"))
    }
}