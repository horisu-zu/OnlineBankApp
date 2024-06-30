package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.onlinebankapp.domain.presentation.cardsection.addcard.AddCardScreen

class AddCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddCardScreen()
        }
    }
}