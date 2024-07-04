package com.example.onlinebankapp.domain.presentation.cardsection.operation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.cardsection.getCardData
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun TopUpOperationScreen(
    paymentCardData: PaymentCardData,
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlightlyGrey),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AnotherGray
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputField(
                    value = inputValue,
                    onValueChange = { inputValue = it},
                    label = "Enter Amount",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                CurrencyCard(paymentCardData = paymentCardData)
            }
        }
    }
}

@Composable
fun CurrencyCard(
    paymentCardData: PaymentCardData
) {
    Card(
        modifier = Modifier
            .border(2.dp, Color.DarkGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = paymentCardData.currency.toString(),
            color = Color.DarkGray,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = SlightlyGrey,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedTextColor = Color.DarkGray,
            focusedTextColor = Color.DarkGray
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Preview
@Composable
fun OperationPreview() {
    OnlineBankAppTheme {
        val cardData = getCardData().get(0)

        Surface(modifier = Modifier.fillMaxSize()) {
            TopUpOperationScreen(paymentCardData = cardData)
        }
    }
}