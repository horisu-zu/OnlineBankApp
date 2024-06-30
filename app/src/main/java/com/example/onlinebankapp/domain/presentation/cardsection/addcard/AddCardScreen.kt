package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun AddCardScreen() {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var isDebit by remember { mutableStateOf(false) }
    var cardColor by remember { mutableStateOf(Color.Black) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardPreview(
            cardNumber = cardNumber,
            isDebit = isDebit,
            expiryMonth = expiryDate.substringBefore("/"),
            expiryYear = expiryDate.substringAfter("/", ""),
            cardColor = cardColor
        )

        CardInput(
            cardNumber = cardNumber,
            onCardNumberChange = { cardNumber = it },
            expiryDate = expiryDate,
            onExpiryDateChange = { expiryDate = it },
            cvv = cvv,
            onCvvChange = { cvv = it },
            cardColor = cardColor,
            onCardColorChange = { cardColor = it },
            isDebit = isDebit,
            onCardTypeChange = { isDebit = it }
        )

        Button(
            onClick = { /*onAddClick*/ },
            modifier = Modifier
                .fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(2.dp)
        ) {
            Text(text = "Add Card", modifier = Modifier.padding(vertical = 6.dp))
        }
    }
}