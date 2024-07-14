package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.data.repository.OperationRepositoryImpl
import com.example.onlinebankapp.data.repository.UserRepositoryImpl
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.addcard.AddCardScreen
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModelFactory
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModelFactory
import com.example.onlinebankapp.domain.repository.CardRepository
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class AddCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OnlineBankAppTheme {
                val firestore = FirebaseFirestore.getInstance()
                val cardRepository = CardRepositoryImpl(firestore)
                val operationRepository = OperationRepositoryImpl(firestore)
                val userRepository = UserRepositoryImpl(firestore, operationRepository)

                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(userRepository)
                )
                val viewModel = remember { CardViewModel(cardRepository) }

                AddCardScreen(viewModel, userViewModel)
            }
        }
    }
}