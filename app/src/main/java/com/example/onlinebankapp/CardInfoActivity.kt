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
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.operation.operationDataList
import com.example.onlinebankapp.domain.presentation.cardsection.carditem.CardItemInfo
import com.example.onlinebankapp.domain.presentation.cardsection.getCardData
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
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

                val cardData by cardViewModel.cardData.collectAsState()
                
                LaunchedEffect(cardId) {
                    cardViewModel.getCardById(cardId)
                }

                CardItemInfo(cardData, emptyList())

                /*val cardData = getCardById(cardId)
                val cardOperations = getOperationsForCard(cardId)*/
            }
        }
    }

    /*private fun getCardById(cardId: String): PaymentCardData {
        return PaymentCardDataRepository.getCardById(cardId)
    }

    private fun getOperationsForCard(cardId: String): List<OperationItemData> {
        return OperationsRepository.getAllOperations().filter { it.cardId == cardId }
    }*/
}

/*
object PaymentCardDataRepository {
    fun getCardById(cardId: String): PaymentCardData {
        return getCardData().first { it.cardName == cardId }
    }
}

object OperationsRepository {
    fun getAllOperations(): List<OperationItemData> {
        return operationDataList()
    }
}*/
