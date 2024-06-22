package com.example.onlinebankapp.domain.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.data.CardType
import com.example.onlinebankapp.data.PaymentCardData
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentCardPager(
    items: List<PaymentCardData>
) {
    val pagerState = rememberPagerState()
    val selectedIndex = pagerState.currentPage

    HorizontalPager(
        count = items.size + 1,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 36.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) { page ->
        if (page < items.size) {
            PaymentCard(paymentCardData = items[page], isSelected = page == selectedIndex)
        } else {
            AddNewCard(isSelected = page == selectedIndex)
        }
    }
}

@Composable
fun PaymentCard(
    isSelected: Boolean,
    paymentCardData: PaymentCardData,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(paymentCardData.cardColor, Color.White),
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
            .padding(12.dp, 8.dp)
            .scale(cardSize),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        /*colors = CardDefaults.cardColors(
            containerColor = paymentCardData.cardColor
        )*/
    ) {
        val textColor = getTextColorForBackground(paymentCardData.cardColor)

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
                    fontSize = 16.sp
                )
                Text(
                    text = "${paymentCardData.expiryMonth}/${paymentCardData.expiryYear}",
                    color = textColor,
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
                    painter = painterResource(id = getCardLogo(paymentCardData.cardType)),
                    contentDescription = paymentCardData.cardType.toString(),
                    modifier = Modifier
                        .size(50.dp)
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
    val firstPart = cardNumber.substring(0, 4)
    val hiddenPart = "**** ****"
    val lastPart = cardNumber.substring(15)

    return "$firstPart  $hiddenPart  $lastPart"
}

fun getCardLogo(cardType: CardType): Int {
    return when (cardType) {
        CardType.VISA -> R.drawable.ic_visa
        CardType.MASTERCARD -> R.drawable.ic_mastercard
        CardType.AMEX -> R.drawable.ic_amex
        CardType.DISCOVER -> R.drawable.ic_discover
        CardType.OTHER -> R.drawable.ic_other
    }
}

fun getTextColorForBackground(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() > 0.5) {
        Color.Black
    } else {
        Color.White
    }
}