package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.CurrencyType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.getCardLogo
import com.example.onlinebankapp.domain.presentation.cardsection.getTextColorForBackground
import com.example.onlinebankapp.domain.presentation.cardsection.hideCardNumber
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun CardHeader(
    paymentCardData: PaymentCardData,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(paymentCardData.cardColor, Color.White),
        start = Offset(0f, 0f),
        end = Offset(1250f, 1250f)
    )

    val textColor = getTextColorForBackground(paymentCardData.cardColor)

    var isCvvVisible by remember { mutableStateOf(false) }
    var isCardNumberVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = gradient)
            .padding(start = 24.dp, end = 24.dp, bottom = 18.dp, top = 54.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = paymentCardData.cardName,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = getElevationColor(paymentCardData.cardColor),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isCardNumberVisible) paymentCardData.cardNumber
                        else hideCardNumber(paymentCardData.cardNumber),
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = getElevationColor(paymentCardData.cardColor),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    modifier = Modifier.clickable { isCardNumberVisible = !isCardNumberVisible }
                )
                Image(
                    painter = painterResource(id = getCardLogo(paymentCardData.cardType)),
                    contentDescription = paymentCardData.cardType.toString(),
                    modifier = Modifier.size(48.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${paymentCardData.expiryMonth}/${paymentCardData.expiryYear}",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = getElevationColor(paymentCardData.cardColor),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .width(80.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.25f)
                    )
                    //elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isCvvVisible = !isCvvVisible },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCvvVisible) paymentCardData.cvv else "CVV2",
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = getElevationColor(paymentCardData.cardColor),
                                    offset = Offset(4f, 4f),
                                    blurRadius = 8f
                                )
                            )
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Balance: ",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${paymentCardData.currentBalance.toInt()} ${paymentCardData.currency}",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            CreditCardButton(onClick = { }, paymentCardData)
        }
    }
}

@Composable
fun CreditCardButton(
    onClick: () -> Unit,
    paymentCardData: PaymentCardData,
    modifier: Modifier = Modifier
) {
    val textColor = getTextColorForBackground(paymentCardData.cardColor)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.25f),
            contentColor = textColor
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_credit),
                contentDescription = "Credit Card Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Credit Card",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

/*@Preview(showBackground = true, widthDp = 360)
@Composable
fun CardHeaderPreview() {
    val sampleCardData = PaymentCardData(
        cardName = "Mastercard Some Card",
        cardNumber = "1234 5678 9012 3456",
        expiryMonth = "12",
        expiryYear = "25",
        cvv = "325",
        cardColor = Color(0xFFC73E9A),
        currentBalance = 1234.56f,
        currency = CurrencyType.JPY,
        cardType = CardType.MASTERCARD
    )

    OnlineBankAppTheme {
        Surface {
            CardHeader(paymentCardData = sampleCardData)
        }
    }
}*/

fun getElevationColor(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() < 0.5) {
        Color.Black
    } else {
        Color.White
    }
}