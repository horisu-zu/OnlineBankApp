package com.example.onlinebankapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlinebankapp.domain.presentation.AppBar
import com.example.onlinebankapp.domain.presentation.BottomNavItem
import com.example.onlinebankapp.domain.presentation.BottomNavigationMenu
import com.example.onlinebankapp.domain.presentation.ExchangeViewModel
import com.example.onlinebankapp.domain.presentation.OperationList
import com.example.onlinebankapp.domain.presentation.YourCardSection
import com.example.onlinebankapp.domain.presentation.viewModelFactory
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.example.onlinebankapp.ui.theme.SlightlyGrey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineBankAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationMenu(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = BottomNavItem.Home.route,
                            enterTransition = getEnterTransition(
                                BottomNavItem.Home.route,
                                BottomNavItem.History.route,
                                BottomNavItem.Services.route
                            ),
                            exitTransition = getExitTransition(
                                BottomNavItem.Home.route,
                                BottomNavItem.History.route,
                                BottomNavItem.Services.route
                            )
                        ) {
                            HomeScreen()
                        }
                        composable(
                            route = BottomNavItem.Services.route,
                            enterTransition = getEnterTransition(
                                BottomNavItem.Services.route,
                                BottomNavItem.Home.route,
                                BottomNavItem.History.route
                            ),
                            exitTransition = getExitTransition(
                                BottomNavItem.Services.route,
                                BottomNavItem.Home.route,
                                BottomNavItem.History.route
                            )
                        ) {
                            HistoryScreen()
                        }
                        composable(
                            route = BottomNavItem.History.route,
                            enterTransition = getEnterTransition(
                                BottomNavItem.History.route,
                                BottomNavItem.Services.route,
                                BottomNavItem.Home.route
                            ),
                            exitTransition = getExitTransition(
                                BottomNavItem.History.route,
                                BottomNavItem.Services.route,
                                BottomNavItem.Home.route
                            )
                        ) {
                            ServicesScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val viewModel = viewModel<ExchangeViewModel>(
        factory = viewModelFactory {
            ExchangeViewModel(OnlineBankApp.appModule.exchangeRepository)
        }
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlightlyGrey),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AppBar(viewModel) {}
            YourCardSection()
            OperationList()
        }
    }
}

@Composable
fun HistoryScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
    ) {}
}

@Composable
fun ServicesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
    ) {}
}

fun getEnterTransition(
    route: String,
    leftRoute: String,
    rightRoute: String
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    when (initialState.destination.route) {
        leftRoute -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        rightRoute -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        else -> null
    }
}

fun getExitTransition(
    route: String,
    leftRoute: String,
    rightRoute: String
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    when (targetState.destination.route) {
        leftRoute -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        rightRoute -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        else -> null
    }
}

/*@Preview(showBackground = true)
@Composable
fun PaymentCardPagerPreview() {
    val items = listOf(
        PaymentCardData(
            cardName = "Visa Classic",
            cardNumber = "1234 5678 9101 1121",
            expiryDate = "12/23",
            cvv = "123",
            currentBalance = 1000f,
            currency = "UAH",
            cardType = CardType.VISA,
            cardColor = Color(0XFF7FFFD4)
        ),
        PaymentCardData(
            cardName = "MasterCard Gold",
            cardNumber = "1111 2222 3333 4444",
            expiryDate = "11/24",
            cvv = "456",
            currentBalance = 2500.12f,
            currency = "EUR",
            cardType = CardType.MASTERCARD,
            cardColor = Color(0xFF1F93B1)
        )
    )

    MaterialTheme {
        PaymentCardPager(items = items)
    }
}*/
