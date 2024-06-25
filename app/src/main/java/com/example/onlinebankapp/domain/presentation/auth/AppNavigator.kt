package com.example.onlinebankapp.domain.presentation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthNavigator(parentNavController: NavController) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, parentNavController) }
        composable("signup") { SignUpScreen(navController, parentNavController) }
    }
}