package com.example.onlinebankapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlinebankapp.data.repository.UserRepositoryImpl
import com.example.onlinebankapp.domain.presentation.auth.AuthNavigator
import com.example.onlinebankapp.domain.navigation.NavigationItemList
import com.example.onlinebankapp.domain.operation.operationDataList
import com.example.onlinebankapp.domain.presentation.AppBar
import com.example.onlinebankapp.domain.presentation.BottomNavItem
import com.example.onlinebankapp.domain.presentation.BottomNavigationMenu
import com.example.onlinebankapp.domain.presentation.viewmodel.exchange.ExchangeViewModel
import com.example.onlinebankapp.domain.presentation.MainNavigationDrawer
import com.example.onlinebankapp.domain.presentation.cardsection.OperationList
import com.example.onlinebankapp.domain.presentation.cardsection.YourCardSection
import com.example.onlinebankapp.domain.presentation.history.HistoryComponent
import com.example.onlinebankapp.domain.presentation.viewmodel.exchange.viewModelFactory
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModelFactory
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.OnlineBankAppTheme
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OnlineBankAppTheme {
                val fireStore = FirebaseFirestore.getInstance()
                val userRepository = UserRepositoryImpl(fireStore)
                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(userRepository)
                )

                MainContent(userViewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: UserViewModel) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    Log.e("AUTH", auth.currentUser?.email.toString())
    val startDestination = if (auth.currentUser != null) "main" else "auth"

    NavHost(navController, startDestination = startDestination) {
        composable("auth") {
            AuthNavigator(navController)
        }
        composable("main") {
            MainAppNavigator(viewModel, navController)
        }
    }
}

@Composable
fun MainAppNavigator(viewModel: UserViewModel, parentNavController: NavController) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            MainNavigationDrawer(
                data = NavigationItemList(),
                drawerState = drawerState,
                scope = scope,
                viewModel,
                parentNavController
            )
        },
        drawerState = drawerState
    ) {
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
                    enterTransition = { getTransitions(BottomNavItem.Home.route,
                        null,
                        BottomNavItem.History.route)()?.let { slideIntoContainer(it) } },
                    exitTransition = { getTransitions(BottomNavItem.Home.route,
                        null,
                        BottomNavItem.History.route)()?.let { slideOutOfContainer(it) } }
                ) {
                    HomeScreen {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }

                composable(
                    route = BottomNavItem.History.route,
                    enterTransition = { getTransitions(BottomNavItem.History.route,
                        BottomNavItem.Home.route,
                        BottomNavItem.Services.route)()?.let { slideIntoContainer(it) } },
                    exitTransition = { getTransitions(BottomNavItem.History.route,
                        BottomNavItem.Home.route,
                        BottomNavItem.Services.route)()?.let { slideOutOfContainer(it) } }
                ) {
                    HistoryScreen()
                }

                composable(
                    route = BottomNavItem.Services.route,
                    enterTransition = { getTransitions(BottomNavItem.Services.route,
                        BottomNavItem.History.route,
                        null)()?.let { slideIntoContainer(it) } },
                    exitTransition = { getTransitions(BottomNavItem.Services.route,
                        BottomNavItem.History.route,
                        null)()?.let { slideOutOfContainer(it) } }
                ) {
                    ServicesScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onMenuClicked : () -> Unit
) {
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
            AppBar(viewModel, onMenuClicked = onMenuClicked) {}
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
            .background(AnotherGray)
    ) {
        HistoryComponent(operationItems = operationDataList())
    }
}

@Composable
fun ServicesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
    ) {}
}

@OptIn(ExperimentalAnimationApi::class)
fun getTransitions(
    route: String,
    leftRoute: String?,
    rightRoute: String?
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> AnimatedContentTransitionScope.SlideDirection? {
    return {
        when {
            (leftRoute != null && initialState.destination.route == leftRoute && targetState.destination.route == route) ||
                    (rightRoute != null && initialState.destination.route == route && targetState.destination.route == rightRoute) ->
                AnimatedContentTransitionScope.SlideDirection.Left

            (rightRoute != null && initialState.destination.route == rightRoute && targetState.destination.route == route) ||
                    (leftRoute != null && initialState.destination.route == route && targetState.destination.route == leftRoute) ->
                AnimatedContentTransitionScope.SlideDirection.Right

            else -> null
        }
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
