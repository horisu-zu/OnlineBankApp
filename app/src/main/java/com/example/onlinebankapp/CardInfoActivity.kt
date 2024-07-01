package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.operation.OperationItemData
import com.example.onlinebankapp.domain.operation.operationDataList
import com.example.onlinebankapp.domain.presentation.cardsection.carditem.CardItemInfo
import com.example.onlinebankapp.domain.presentation.cardsection.getCardData
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme

class CardInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra("cardId") ?: return

        setContent {
            OnlineBankAppTheme {
                val cardData = getCardById(cardId)
                val cardOperations = getOperationsForCard(cardId)

                CardItemInfo(cardData, cardOperations)
            }
        }
    }

    private fun getCardById(cardId: String): PaymentCardData {
        return PaymentCardDataRepository.getCardById(cardId)
    }

    private fun getOperationsForCard(cardId: String): List<OperationItemData> {
        return OperationsRepository.getAllOperations().filter { it.cardId == cardId }
    }
}

object PaymentCardDataRepository {
    fun getCardById(cardId: String): PaymentCardData {
        return getCardData().first { it.cardName == cardId }
    }
}

object OperationsRepository {
    fun getAllOperations(): List<OperationItemData> {
        return operationDataList()
    }
}