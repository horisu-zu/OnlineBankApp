package com.example.onlinebankapp.domain.presentation.cardsection.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.domain.presentation.shimmerEffect
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.user.UserData
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun CheckCardComponent(viewModel: CardViewModel, userViewModel: UserViewModel) {
    val cardData by viewModel.cardData.collectAsState()
    val userState by userViewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getCurrentUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardPreview(cardData = cardData)

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Card Information", style = MaterialTheme.typography.titleMedium)
                Text("Card Type: ${cardData.cardType}")
                Text("Card Number: ${cardData.cardNumber}")
                Text("Card Service : ${cardData.cardService}")
                Text("Expiry Date: ${cardData.expiryMonth}/${cardData.expiryYear}")
                Text("CVV: ${cardData.cvv}")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("User Information", style = MaterialTheme.typography.titleMedium)
                when (userState) {
                    is Resource.Success -> {
                        val userData = (userState as Resource.Success<UserData>).data
                        Text("User Name: ${userData?.userName}")
                        //Text(text = "User ID: ${cardData.ownerId}")
                    }
                    is Resource.Error -> {
                        Text("Error loading user data: ${(userState as Resource.Error).message}")
                    }
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .shimmerEffect()
                        )
                        /*Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .shimmerEffect()
                        )*/
                    }
                }
            }
        }
    }
}