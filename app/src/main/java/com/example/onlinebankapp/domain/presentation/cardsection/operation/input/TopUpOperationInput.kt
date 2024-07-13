package com.example.onlinebankapp.domain.presentation.cardsection.operation.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.PaymentCard
import com.example.onlinebankapp.domain.presentation.cardsection.SmallPaymentCard
import com.example.onlinebankapp.domain.presentation.template.CurrencyCard
import com.example.onlinebankapp.domain.presentation.template.InputField
import com.example.onlinebankapp.domain.presentation.template.OperationButton
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TopUpOperationInput(
    cardData: List<PaymentCardData>,
    initialCardIndex: Int,
    inputAmount: String,
    onAmountEntered: (String, PaymentCardData?, PaymentCardData?) -> Unit
) {
    var localInputAmount by remember { mutableStateOf(inputAmount) }
    val pagerState = rememberPagerState(initialPage = initialCardIndex)
    val selectedIndex = pagerState.currentPage
    val selectedCard = cardData[selectedIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AnotherGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SlightlyGrey
                )
            ) {
                HorizontalPager(
                    count = cardData.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {page ->
                    SmallPaymentCard(cardData = cardData[page],
                        isSelected = page == selectedIndex
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputField(
                    value = localInputAmount,
                    onValueChange = { localInputAmount = it },
                    label = "Enter Amount",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                CurrencyCard(paymentCardData = selectedCard)
            }
            OperationButton(
                onClick = { onAmountEntered(localInputAmount, null, selectedCard)},
                enabled = localInputAmount.isNotEmpty()
            )
        }
    }
}