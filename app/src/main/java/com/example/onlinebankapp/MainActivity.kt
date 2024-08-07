package com.example.onlinebankapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlinebankapp.data.repository.CardRepositoryImpl
import com.example.onlinebankapp.data.repository.OperationRepositoryImpl
import com.example.onlinebankapp.data.repository.TransactionRepositoryImpl
import com.example.onlinebankapp.data.repository.UserRepositoryImpl
import com.example.onlinebankapp.domain.presentation.auth.AuthNavigator
import com.example.onlinebankapp.domain.navigation.NavigationItemList
import com.example.onlinebankapp.domain.operation.OperationData
import com.example.onlinebankapp.domain.operation.operationDataList
import com.example.onlinebankapp.domain.presentation.AppBar
import com.example.onlinebankapp.domain.presentation.BottomNavItem
import com.example.onlinebankapp.domain.presentation.BottomNavigationMenu
import com.example.onlinebankapp.domain.presentation.viewmodel.exchange.ExchangeViewModel
import com.example.onlinebankapp.domain.presentation.MainNavigationDrawer
import com.example.onlinebankapp.domain.presentation.cardsection.OperationComponent
import com.example.onlinebankapp.domain.presentation.cardsection.OperationList
import com.example.onlinebankapp.domain.presentation.cardsection.YourCardSection
import com.example.onlinebankapp.domain.presentation.history.HistoryComponent
import com.example.onlinebankapp.domain.presentation.service.ServiceScreen
import com.example.onlinebankapp.domain.presentation.template.OperationButton
import com.example.onlinebankapp.domain.presentation.viewmodel.card.CardViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.exchange.viewModelFactory
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.TransactionViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModelFactory
import com.example.onlinebankapp.domain.util.Resource
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
                val operationRepository = OperationRepositoryImpl(fireStore)
                val userRepository = UserRepositoryImpl(fireStore, operationRepository)
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
    val firestore = FirebaseFirestore.getInstance()
    Log.e("AUTH", auth.currentUser?.email.toString())
    val startDestination = if (auth.currentUser != null) "main" else "auth"

    NavHost(navController, startDestination = startDestination) {
        composable("auth") {
            AuthNavigator(navController)
        }
        composable("main") {
            MainAppNavigator(viewModel, navController, firestore, auth.uid.toString())
        }
    }
}

@Composable
fun MainAppNavigator(viewModel: UserViewModel, parentNavController: NavController,
                     firestore: FirebaseFirestore, userId: String) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val cardRepository = CardRepositoryImpl(firestore)
    val cardViewModel = remember { CardViewModel(cardRepository) }

    val operationRepository = OperationRepositoryImpl(firestore)
    val operationViewModel = remember { OperationViewModel(operationRepository) }

    val transactionRepository = TransactionRepositoryImpl(firestore)
    val transactionViewModel = remember { TransactionViewModel(transactionRepository) }

    val userRepository = UserRepositoryImpl(firestore, operationRepository)
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository)
    )

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
                    HomeScreen(
                        onMenuClicked = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        userViewModel = userViewModel,
                        cardViewModel = cardViewModel,
                        operationViewModel = operationViewModel,
                        userId = userId
                    )
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
                    HistoryScreen(
                        userId = userId,
                        transactionViewModel = transactionViewModel,
                        operationViewModel = operationViewModel
                    )
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
                    ServicesScreen(operationViewModel)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onMenuClicked: () -> Unit,
    cardViewModel: CardViewModel,
    userViewModel: UserViewModel,
    operationViewModel: OperationViewModel,
    userId: String
) {
    val viewModel = viewModel<ExchangeViewModel>(
        factory = viewModelFactory {
            ExchangeViewModel(OnlineBankApp.appModule.exchangeRepository)
        }
    )

    val operationsState by operationViewModel.operationState.collectAsState()
    val quickOperationsState by userViewModel.quickOperations.collectAsState()
    val selectedOperationsState by operationViewModel.selectedOperations.collectAsState()

    LaunchedEffect(userId) {
        userViewModel.loadQuickOperations(userId)
    }

    LaunchedEffect(quickOperationsState) {
        if (quickOperationsState is Resource.Success) {
            val operationIds = (quickOperationsState as Resource.Success).data ?: emptyList()
            operationViewModel.getOperationsByIds(operationIds)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AppBar(viewModel, onMenuClicked = onMenuClicked)
        YourCardSection(cardViewModel, userId)
        OperationComponent(
            quickOperationsState = quickOperationsState,
            operations = operationsState,
            selectedOperationsState = selectedOperationsState,
            onSelectedOperationsChange = { newSelectedOperations ->
                val newSelectedIds = newSelectedOperations.map { it.operationId }
                operationViewModel.getOperationsByIds(newSelectedIds)
            },
            onSaveClick = { selectedIds ->
                Log.d("Selected IDs", selectedIds.size.toString())
                Log.d("Selected IDs", selectedIds.toString())
                userViewModel.updateQuickOperations(userId, selectedIds)
            }
        )
    }
}

@Composable
fun HistoryScreen(
    userId: String,
    transactionViewModel: TransactionViewModel,
    operationViewModel: OperationViewModel
) {
    val transactionState by transactionViewModel.transactionData.collectAsState()

    LaunchedEffect(Unit) {
        transactionViewModel.getTransactionsBy(userId)
        operationViewModel.getOperations()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AnotherGray)
    ) {
        when (val state = transactionState) {
            is Resource.Success -> {
                HistoryComponent(
                    operationItemsData = state.data!!,
                    operationViewModel = operationViewModel
                )
            }
            is Resource.Loading -> {}
            is Resource.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
                OperationButton(
                    label = "Retry",
                    onClick = {
                    transactionViewModel.getTransactionsBy(userId)
                    operationViewModel.getOperations()
                              },
                    enabled = true)
            }
        }
    }
}

@Composable
fun ServicesScreen(
    operationViewModel: OperationViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
    ) {
        ServiceScreen(operationViewModel = operationViewModel)
    }
}

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
