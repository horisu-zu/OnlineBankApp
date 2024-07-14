package com.example.onlinebankapp.domain.presentation.auth

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlinebankapp.data.repository.OperationRepositoryImpl
import com.example.onlinebankapp.data.repository.UserRepositoryImpl
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AuthNavigator(parentNavController: NavController) {
    val navController = rememberNavController()
    val fireStore = FirebaseFirestore.getInstance()
    val operationRepository = OperationRepositoryImpl(fireStore)
    val userRepository = UserRepositoryImpl(fireStore, operationRepository)
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository)
    )

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, parentNavController, userViewModel) }
        composable("signup") { SignUpScreen(navController, parentNavController, userViewModel) }
    }
}