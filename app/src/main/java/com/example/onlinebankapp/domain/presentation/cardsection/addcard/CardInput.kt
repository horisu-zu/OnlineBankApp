package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.CardType
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.presentation.template.toColor
import com.example.onlinebankapp.domain.presentation.template.toHexString
import com.example.onlinebankapp.ui.theme.Aquamarine
import com.example.onlinebankapp.ui.theme.Coral
import com.example.onlinebankapp.ui.theme.Lavender
import com.example.onlinebankapp.ui.theme.Mauve
import com.example.onlinebankapp.ui.theme.Ochre
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import com.example.onlinebankapp.ui.theme.Teal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardInput(
    cardData: PaymentCardData,
    onCardDataChange: (PaymentCardData) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    var isAmex by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlightlyGrey)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardTypeSelector(
            isDebit = cardData.cardType == CardType.DEBIT,
            onCardTypeChange = { isDebit ->
                onCardDataChange(cardData.copy(cardType = if (isDebit) CardType.DEBIT
                else CardType.CREDIT))
            }
        )

        TextField(
            value = cardData.cardNumber,
            onValueChange = { cardNumber ->
                if (cardNumber.length <= 16) {
                    isAmex = cardNumber.startsWith("3")
                    val formattedNumber = cardNumber.replace("\\s".toRegex(), "")
                    onCardDataChange(cardData.copy(
                        cardNumber = formattedNumber,
                        cardService = getCardServiceFromNumber(formattedNumber)
                    ))
                }
            },
            label = { Text("Card Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = SlightlyGrey,
                focusedIndicatorColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.Gray,
                unfocusedTextColor = Color.DarkGray,
                focusedTextColor = Color.DarkGray
            ),
            singleLine = true,
            visualTransformation = formatInputNumber(cardData.cardNumber)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val months = (1..12).map { it.toString().padStart(2, '0') }
            val years = (2024..2029).map { it.toString() }

            DateSelector(
                selectedValue = cardData.expiryMonth,
                onValueSelected = { month ->
                    onCardDataChange(cardData.copy(expiryMonth = month))
                },
                label = "Month",
                options = months,
                modifier = Modifier.weight(1f)
            )

            DateSelector(
                selectedValue = cardData.expiryYear,
                onValueSelected = { year ->
                    onCardDataChange(cardData.copy(expiryYear = year))
                },
                label = "Year",
                options = years,
                modifier = Modifier.weight(1f)
            )

            TextField(
                value = cardData.cvv,
                onValueChange = {
                    if ((isAmex && it.length <= 4) || (!isAmex && it.length <= 3)) {
                        onCardDataChange(cardData.copy(cvv = it))
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Image(
                            painter = if (passwordVisibility)
                                painterResource(id = R.drawable.visible)
                            else painterResource(id = R.drawable.hidden),
                            modifier = Modifier.size(24.dp),
                            contentDescription = if (passwordVisibility) "Hide password"
                            else "Show password"
                        )
                    }
                },
                label = { Text(text = "CVV", fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = SlightlyGrey,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.Gray,
                    unfocusedTextColor = Color.DarkGray,
                    focusedTextColor = Color.DarkGray
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                singleLine = true
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            ColorSelector(
                selectedColor = cardData.cardColor.toColor(),
                onColorSelected = { onCardDataChange(cardData.copy(cardColor = it.toHexString())) }
            )
        }
    }
}

@Composable
fun ColorSelector(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color.Black,
        Color.White,
        Aquamarine,
        Coral,
        Lavender,
        Teal,
        Mauve,
        Ochre
    )

    LazyRow(
        modifier = Modifier.background(SlightlyGrey),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        items(colors) { color ->
            ColorItem(
                color = color,
                isSelected = color == selectedColor,
                onColorSelected = { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun ColorItem(
    color: Color,
    isSelected: Boolean,
    onColorSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onColorSelected)
    )
}

@Composable
fun CardTypeSelector(isDebit: Boolean, onCardTypeChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CardType(
            text = "Credit",
            isSelected = !isDebit,
            onClick = { onCardTypeChange(false) },
            modifier = Modifier.weight(1f)
        )
        CardType(
            text = "Debit",
            isSelected = isDebit,
            onClick = { onCardTypeChange(true) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CardType(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(96.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) Color.DarkGray else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Black.copy(alpha = 0.1f) else Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.DarkGray else Color.Black
            )
        }
    }
}

fun formatInputNumber(text: String): VisualTransformation {
    return VisualTransformation { text ->
        val trimmed = text.text.take(16)
        val formatted = trimmed.chunked(4).joinToString(" ")

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 4) offset else if (offset <= 8) offset + 1
                else if (offset <= 12) offset + 2 else offset + 3

            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 4) offset else if (offset <= 9) offset - 1
                else if (offset <= 14) offset - 2 else offset - 3
        }

        TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
