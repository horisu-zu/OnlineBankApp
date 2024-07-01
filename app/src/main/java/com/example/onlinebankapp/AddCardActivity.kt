package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.addcard.AddCardScreen
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModelFactory
import com.example.onlinebankapp.domain.repository.CardRepository
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class AddCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firestore = FirebaseFirestore.getInstance()
        val cardRepository = CardRepositoryImpl(firestore)

        setContent {
            OnlineBankAppTheme {
                val viewModel = remember { CardViewModel(cardRepository) }
                AddCardScreen(viewModel)
            }
        }
    }
}