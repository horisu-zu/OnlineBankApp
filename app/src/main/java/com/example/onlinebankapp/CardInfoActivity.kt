package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.data.repository.OperationRepositoryImpl
import com.example.onlinebankapp.data.repository.TransactionRepositoryImpl
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.operation.operationDataList
import com.example.onlinebankapp.domain.presentation.cardsection.carditem.CardItemInfo
import com.example.onlinebankapp.domain.presentation.cardsection.getCardData
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.TransactionViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class CardInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra("cardId") ?: return

        setContent {
            OnlineBankAppTheme {
                val firestore = FirebaseFirestore.getInstance()
                val cardRepository = CardRepositoryImpl(firestore)
                val cardViewModel = remember { CardViewModel(cardRepository) }

                val transactionRepo = TransactionRepositoryImpl(firestore)
                val transactionViewModel = remember { TransactionViewModel(transactionRepo)}

                val operationRepo = OperationRepositoryImpl(firestore)
                val operationViewModel = remember { OperationViewModel(operationRepo)}

                val cardData by cardViewModel.cardData.collectAsState()
                val transactionData by transactionViewModel.transactionData.collectAsState()
                
                LaunchedEffect(cardId) {
                    cardViewModel.getCardById(cardId)
                    transactionViewModel.getTransactionsFor(cardId)
                }

                when (transactionData) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        CardItemInfo(
                            paymentCardData = cardData,
                            operations = transactionData.data!!,
                            operationViewModel = operationViewModel
                        )
                    }
                    is Resource.Error -> {
                        Text(text = transactionData.message ?: "Unknown error")
                    }
                }
            }
        }
    }
}
