package com.example.onlinebankapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.data.repository.OperationRepositoryImpl
import com.example.onlinebankapp.data.repository.TransactionRepositoryImpl
import com.example.onlinebankapp.domain.presentation.cardsection.operation.OperationScreen
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.TransactionViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OperationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra("cardId") ?: ""
        val operationDataId = intent.getStringExtra("operationDataId") ?: return
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        setContent {
            OnlineBankAppTheme {
                val firestore = FirebaseFirestore.getInstance()

                val cardRepository = CardRepositoryImpl(firestore)
                val operationRepository = OperationRepositoryImpl(firestore)
                val transactionRepo = TransactionRepositoryImpl(firestore)

                val cardViewModel = remember { CardViewModel(cardRepository) }
                val operationViewModel = remember { OperationViewModel(operationRepository) }
                val transactionViewModel = remember { TransactionViewModel(transactionRepo) }

                LaunchedEffect(userId) {
                    cardViewModel.setUserId(userId.toString())
                    operationViewModel.getOperations()
                }

                val userCardsResource by cardViewModel.userCards.collectAsState()
                val operationsResource by operationViewModel.operationState.collectAsState()

                when (val cardsResource = userCardsResource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val userCards = cardsResource.data ?: emptyList()
                        val initialCardIndex = userCards.indexOfFirst{
                            it.cardId == cardId
                        }.takeIf { it != -1 } ?: 0

                        when (val opsResource = operationsResource) {
                            is Resource.Loading -> {
                            }
                            is Resource.Success -> {
                                val operations = opsResource.data ?: emptyList()
                                val operationData = operations.find { it.operationId == operationDataId }
                                if (operationData != null) {
                                    OperationScreen(
                                        operationData = operationData,
                                        onBackPressed = { onBackPressed() },
                                        viewModel = cardViewModel,
                                        transactionViewModel = transactionViewModel,
                                        userCards = userCards,
                                        initialCardIndex = initialCardIndex
                                    )
                                } else {
                                    Text("Operation not found")
                                }
                            }
                            is Resource.Error -> {
                                Text("Error loading operations: ${opsResource.message}")
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text("Error loading cards: ${cardsResource.message}")
                    }
                }
            }
        }
    }
}