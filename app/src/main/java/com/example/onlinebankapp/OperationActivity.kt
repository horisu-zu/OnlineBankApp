package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.OperationType
import com.example.onlinebankapp.domain.operation.SampleOperations
import com.example.onlinebankapp.domain.presentation.cardsection.operation.OperationScreen
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class OperationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra("cardId") ?: return
        val operationDataId = intent.getStringExtra("operationDataId") ?: return

        val operationData = SampleOperations.operations.find {
            it.operationId == operationDataId
        } ?: return

        setContent {
            OnlineBankAppTheme {
                val firestore = FirebaseFirestore.getInstance()
                val cardRepository = CardRepositoryImpl(firestore)
                val cardViewModel = remember { CardViewModel(cardRepository) }

                LaunchedEffect(cardId) {
                    cardViewModel.getCardById(cardId)
                }

                OperationScreen(operationData, { onBackPressed() }, cardViewModel)
            }
        }
    }
}