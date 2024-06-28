package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
            .padding(horizontal = 36.dp, vertical = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = paymentCardData.cardName,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    text = if (isCvvVisible) paymentCardData.cvv else "***",
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable { isCvvVisible = !isCvvVisible }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Balance: ",
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${paymentCardData.currentBalance.toInt()} ${paymentCardData.currency}",
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
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
            containerColor = Color.White.copy(alpha = 0.2f),
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

@Preview(showBackground = true, widthDp = 360)
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
}