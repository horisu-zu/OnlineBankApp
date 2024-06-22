package com.example.onlinebankapp.domain.presentation

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.R

@Composable
fun ExchangeRateCard(
    firstCurrency: String,
    secondCurrency: String,
    toCurrency: String,
    viewModel: ExchangeViewModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_dollar),
                contentDescription = "Currency Exchange",
                modifier = Modifier.size(24.dp)
            )
            /*Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$firstCurrency/$toCurrency: $firstRate")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "$secondCurrency/$toCurrency: $secondRate")*/
        }
    }
}
