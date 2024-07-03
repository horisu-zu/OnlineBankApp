package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(viewModel: CardViewModel, userViewModel: UserViewModel) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 3
    val cardData by viewModel.cardData.collectAsState()
    val isEnabled = currentStep < totalSteps && isCardDataValid(cardData)

    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        cardData.ownerId = currentUser.uid
    }

    val scope = rememberCoroutineScope()
    val addCardState by viewModel.addCardState.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity

    val handleBackNavigation = {
        when (currentStep) {
            1 -> activity?.onBackPressed()
            2 -> currentStep--
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { StepIndicator(
                    stepsCount = totalSteps,
                    currentStep = currentStep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) },
                navigationIcon = {
                    IconButton(
                        onClick = { handleBackNavigation() },
                        enabled = currentStep < 3
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (currentStep < 3) Color.DarkGray else Color.LightGray
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (currentStep < totalSteps && isCardDataValid(cardData)) {
                                if (currentStep == 2) {
                                    scope.launch {
                                        viewModel.addCard(cardData)
                                    }
                                }
                                currentStep++
                            }
                        },
                        enabled = currentStep < totalSteps && isCardDataValid(cardData)
                    ) {
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = if(isEnabled) Color.DarkGray else Color.LightGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SlightlyGrey
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentStep) {
                1 -> AddCardComponent(viewModel)
                2 -> CheckCardComponent(viewModel, userViewModel)
                3 -> ConfirmationComponent(
                    addCardState = addCardState,
                    onRetry = {
                        scope.launch {
                            viewModel.addCard(cardData)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddCardComponent(viewModel: CardViewModel) {
    val cardData by viewModel.cardData.collectAsState()
    var validationErrors by remember { mutableStateOf(mapOf<String, String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardPreview(cardData = cardData)
        CardInput(
            cardData = cardData,
            onCardDataChange = { viewModel.updateCardData(it) }
        )

        validationErrors.forEach { (field, error) ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun isCardDataValid(cardData: PaymentCardData): Boolean {
    return validateCardData(cardData).isEmpty()
}

fun validateCardData(cardData: PaymentCardData): Map<String, String> {
    val errors = mutableMapOf<String, String>()

    if (cardData.cardNumber.length != 16 && cardData.cardNumber.length != 15) {
        errors["cardNumber"] = "Card number must have 16 digits"
    }

    val expectedCvvLength = if (cardData.cardService == CardService.AMEX) 4 else 3
    if (cardData.cvv.length != expectedCvvLength) {
        errors["cvv"] = "CVV must be $expectedCvvLength digits"
    }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    val cardYear = cardData.expiryYear.toIntOrNull()?.minus(2000) ?: -1
    val cardMonth = cardData.expiryMonth.toIntOrNull() ?: -1

    if (cardYear < currentYear || (cardYear == currentYear && cardMonth < currentMonth)) {
        errors["expiryDate"] = "Card has expired"
    }

    return errors
}