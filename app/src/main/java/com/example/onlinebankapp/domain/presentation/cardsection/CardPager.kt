package com.example.onlinebankapp.domain.presentation.cardsection

import android.content.Intent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.AddCardActivity
import com.example.onlinebankapp.CardInfoActivity
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CardService
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.card.toColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentCardPager(
    items: List<PaymentCardData>
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val selectedIndex = pagerState.currentPage

    HorizontalPager(
        count = items.size + 1,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        itemSpacing = 8.dp
    ) { page ->
        if (page < items.size) {
            PaymentCard(paymentCardData = items[page],
                isSelected = page == selectedIndex,
                onCardClick = { cardId ->
                    val intent = Intent(context, CardInfoActivity::class.java).apply {
                        putExtra("cardId", cardId)
                    }
                    context.startActivity(intent)
                })
        } else {
            AddNewCard(isSelected = page == selectedIndex)
        }
    }
}

@Composable
fun PaymentCard(
    isSelected: Boolean,
    paymentCardData: PaymentCardData,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit
) {
    val gradient = Brush.linearGradient(
        colors = listOf(paymentCardData.cardColor.toColor(), Color.White),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(750f, 750f)
    )

    val cardSize by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.95f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        ),
        label = ""
    )

    Card(
        modifier = modifier
            //.padding(12.dp, 8.dp)
            .scale(cardSize)
            .clickable { onCardClick(paymentCardData.cardId) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        /*colors = CardDefaults.cardColors(
            containerColor = paymentCardData.cardColor
        )*/
    ) {
        val textColor = getTextColorForBackground(paymentCardData.cardColor.toColor())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradient)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = paymentCardData.cardName,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = hideCardNumber(paymentCardData.cardNumber),
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatExpiryDate(paymentCardData),
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${paymentCardData.currentBalance.toInt()} ${paymentCardData.currency}",
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Image(
                    painter = painterResource(id = getCardLogo(paymentCardData.cardService)),
                    contentDescription = paymentCardData.cardType.toString(),
                    modifier = Modifier
                        .size(60.dp)
                )
            }
        }
    }
}

@Composable
fun AddNewCard(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val cardSize by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.95f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        ),
        label = ""
    )
    val context = LocalContext.current

    Card(
        modifier = modifier
            .padding(12.dp, 8.dp)
            .scale(cardSize),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Gray, Color.LightGray),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(750f, 750f)
                    )
                )
                .clickable {
                    val intent = Intent(context, AddCardActivity::class.java)
                    context.startActivity(intent)
                }
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add New Card",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

fun hideCardNumber(cardNumber: String): String {
    val firstPart = cardNumber.take(4)
    val hiddenPart = "**** ****"
    val lastPart = cardNumber.takeLast(4)

    return "$firstPart  $hiddenPart  $lastPart"
}

fun formatExpiryDate(paymentCardData: PaymentCardData): String {
    val expiryYear = paymentCardData.expiryYear.takeLast(2)
    return "${paymentCardData.expiryMonth}/$expiryYear"
}

fun getCardLogo(cardService: CardService): Int {
    return when (cardService) {
        CardService.VISA -> R.drawable.ic_visa
        CardService.MASTERCARD -> R.drawable.ic_mastercard
        CardService.AMEX -> R.drawable.ic_amex
        CardService.DISCOVER -> R.drawable.ic_discover
        CardService.OTHER -> R.drawable.ic_other
    }
}

fun getTextColorForBackground(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() > 0.5) {
        Color.Black
    } else {
        Color.White
    }
}