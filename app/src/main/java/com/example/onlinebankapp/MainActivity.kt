package com.example.onlinebankapp

import android.os.Bundle
import android.service.autofill.OnClickAction
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.data.CardType
import com.example.onlinebankapp.data.PaymentCardData
import com.example.onlinebankapp.domain.presentation.ExchangeViewModel
import com.example.onlinebankapp.domain.presentation.MyAppBar
import com.example.onlinebankapp.domain.presentation.OperationList
import com.example.onlinebankapp.domain.presentation.PaymentCardPager
import com.example.onlinebankapp.domain.presentation.YourCardSection
import com.example.onlinebankapp.domain.presentation.getCardData
import com.example.onlinebankapp.domain.presentation.getOperationTypeData
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    //private val viewModel: ExchangeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineBankAppTheme {
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5))
                    ) {
                        MyAppBar() {}
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
