package com.example.onlinebankapp.domain.presentation

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.exchange.ExchangeData
import com.example.onlinebankapp.domain.presentation.viewmodel.exchange.ExchangeViewModel
import com.example.onlinebankapp.domain.util.Resource

@Composable
fun ExchangeRateCard(
    firstCurrency: String,
    secondCurrency: String,
    viewModel: ExchangeViewModel
) {
    Card(
        modifier = Modifier
            .wrapContentWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ExchangeRow(
                currencyName = firstCurrency,
                viewModel = viewModel
            )
            Text(
                text = "/",
                style = MaterialTheme.typography.bodyMedium
            )
            ExchangeRow(
                currencyName = secondCurrency,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ExchangeRow(
    currencyName: String,
    viewModel: ExchangeViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(currencyName) {
        viewModel.fetchConversionRate(currencyName)
    }

    val conversionRateResource by viewModel.getConversionRateLiveData(currencyName).observeAsState()

    Row(
        modifier = modifier
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            painter = painterResource(id = getIconForCurrency(currencyName)),
            tint = Color.DarkGray,
            contentDescription = "Currency Icon for $currencyName",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = when (conversionRateResource) {
                is Resource.Success -> {
                    val rate = (conversionRateResource as Resource.Success<ExchangeData>)
                        .data?.conversion_rate
                    val formattedRate = String.format("%.2f", rate ?: 0.00)
                    formattedRate
                }
                is Resource.Error -> "Error"
                else -> "..."
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun getIconForCurrency(
    currencyName: String
): Int {
    return when(currencyName) {
        "USD" -> R.drawable.ic_dollar
        "EUR" -> R.drawable.ic_euro
        "GBP" -> R.drawable.ic_pound
        "JPY" -> R.drawable.ic_yen
        else -> R.drawable.ic_money
    }
}
