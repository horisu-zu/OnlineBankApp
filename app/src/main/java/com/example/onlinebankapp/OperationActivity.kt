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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.operation.SampleOperations
import com.example.onlinebankapp.domain.presentation.cardsection.operation.OperationScreen
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OperationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra("cardId") ?: return
        val operationDataId = intent.getStringExtra("operationDataId") ?: return
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val operationData = SampleOperations.operations.find {
            it.operationId == operationDataId
        } ?: return

        setContent {
            OnlineBankAppTheme {
                val firestore = FirebaseFirestore.getInstance()
                val cardRepository = CardRepositoryImpl(firestore)
                val cardViewModel = remember { CardViewModel(cardRepository) }

                LaunchedEffect(userId) {
                    cardViewModel.setUserId(userId.toString())
                }

                val userCardsResource by cardViewModel.userCards.collectAsState()

                when (val resource = userCardsResource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val userCards = resource.data ?: emptyList()
                        Log.d("OperationActivity", "Cards loaded successfully: $userCards")
                        val initialCardIndex =
                            userCards.indexOfFirst { it.cardId == cardId }.takeIf { it != -1 } ?: 0
                        OperationScreen(
                            operationData = operationData,
                            onBackPressed = { onBackPressed() },
                            viewModel = cardViewModel,
                            userCards = userCards,
                            initialCardIndex = initialCardIndex
                        )
                    }
                    is Resource.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error loading cards: ${resource.message}")
                        }
                    }
                }
            }
        }
    }
}