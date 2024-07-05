package com.example.onlinebankapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.domain.presentation.cardsection.operation.TopUpOperationScreen
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class OperationActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra("cardId") ?: return
        var operationType = intent.getStringExtra("operationType") ?: return

        setContent {
            OnlineBankAppTheme {
                val context = LocalContext.current
                val firestore = FirebaseFirestore.getInstance()
                val cardRepository = CardRepositoryImpl(firestore)
                val cardViewModel = remember { CardViewModel(cardRepository) }

                val cardData by cardViewModel.cardData.collectAsState()

                LaunchedEffect(cardId) {
                    cardViewModel.getCardById(cardId)
                }

                TopUpOperationScreen(cardData, { onBackPressed(context) })
            }
        }
    }
}

private fun onBackPressed(
    context: Context
) {
    val activity = context as? Activity

    activity?.onBackPressed()
}