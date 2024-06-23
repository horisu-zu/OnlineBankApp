package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.onlinebankapp.domain.presentation.ExchangeViewModel
import com.example.onlinebankapp.domain.presentation.MyAppBar
import com.example.onlinebankapp.domain.presentation.OperationList
import com.example.onlinebankapp.domain.presentation.YourCardSection
import com.example.onlinebankapp.domain.presentation.viewModelFactory
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.example.onlinebankapp.ui.theme.SlightlyGrey

class MainActivity : ComponentActivity() {

    //private val viewModel: ExchangeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineBankAppTheme {
                val viewModel = viewModel<ExchangeViewModel>(
                    factory = viewModelFactory {
                        ExchangeViewModel(OnlineBankApp.appModule.exchangeRepository)
                    }
                )

                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(SlightlyGrey)
                    ) {
                        MyAppBar(viewModel) {}
                        YourCardSection()
                        OperationList()
                    }
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun PaymentCardPagerPreview() {
    val items = listOf(
        PaymentCardData(
            cardName = "Visa Classic",
            cardNumber = "1234 5678 9101 1121",
            expiryDate = "12/23",
            cvv = "123",
            currentBalance = 1000f,
            currency = "UAH",
            cardType = CardType.VISA,
            cardColor = Color(0XFF7FFFD4)
        ),
        PaymentCardData(
            cardName = "MasterCard Gold",
            cardNumber = "1111 2222 3333 4444",
            expiryDate = "11/24",
            cvv = "456",
            currentBalance = 2500.12f,
            currency = "EUR",
            cardType = CardType.MASTERCARD,
            cardColor = Color(0xFF1F93B1)
        )
    )

    MaterialTheme {
        PaymentCardPager(items = items)
    }
}*/
