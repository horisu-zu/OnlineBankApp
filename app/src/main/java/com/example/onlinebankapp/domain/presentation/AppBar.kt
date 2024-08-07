package com.example.onlinebankapp.domain.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.presentation.viewmodel.exchange.ExchangeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    viewModel: ExchangeViewModel,
    onMenuClicked: () -> Unit = {},
    onNotificationClicked: () -> Unit = {},
    onCommunicationClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.Black
            ),
            title = {
                ExchangeRateCard(
                    firstCurrency = "USD",
                    secondCurrency = "EUR",
                    viewModel = viewModel
                )
            },
            navigationIcon = {
                IconButton(onClick = onMenuClicked) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(onClick = onNotificationClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "Notification Icon",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onCommunicationClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_communication),
                        contentDescription = "Communication Icon",
                        tint = Color.Black
                    )
                }
            }
        )
    }
}