package com.example.onlinebankapp.domain.presentation.cardsection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardBottomSheet(
    items: List<PaymentCardData>,
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = SlightlyGrey
        ) {
            PaymentCardList(cards = items)
        }
        LaunchedEffect(sheetState) {
            scope.launch { sheetState.show() }
        }
    }
}

@Composable
fun PaymentCardList(cards: List<PaymentCardData>) {
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 36.dp)
    ) {
        items(cards) { card ->
            PaymentCardItem(cardData = card)
        }
    }
}

@Composable
fun PaymentCardItem(cardData: PaymentCardData) {
    val gradient = Brush.linearGradient(
        colors = listOf(cardData.cardColor, Color.White),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(200f, 200f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .size(width = 96.dp, height = 56.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.elevatedCardElevation(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = getCardLogo(cardData.cardType)),
                    contentDescription = cardData.cardType.toString(),
                    modifier = Modifier
                        .size(36.dp)
                )
                Text(
                    text = "•••• ${cardData.cardNumber.takeLast(4)}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            Text(
                text = cardData.cardName,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            val formattedBalance = "%.2f".format(Locale.ENGLISH, cardData.currentBalance)
            Text(
                text = "$formattedBalance ${cardData.currency}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}