package com.example.onlinebankapp.domain.presentation.service

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onlinebankapp.domain.presentation.viewmodel.operation.OperationViewModel

@Composable
fun ServiceScreen(
    operationViewModel: OperationViewModel
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "serviceList") {
        composable(
            route = "serviceList",
            enterTransition = { slideInTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            popEnterTransition = { slideInTransition(AnimatedContentTransitionScope.SlideDirection.Right) },
            popExitTransition = { slideOutTransition(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) {
            ServicesListScreen(navController, operationViewModel)
        }
        composable(
            route = "operationList/{typeId}",
            enterTransition = { slideInTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutTransition(AnimatedContentTransitionScope.SlideDirection.Left) },
            popEnterTransition = { slideInTransition(AnimatedContentTransitionScope.SlideDirection.Right) },
            popExitTransition = { slideOutTransition(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) { backStackEntry ->
            OperationListScreen(
                typeId = backStackEntry.arguments?.getString("typeId") ?: "",
                operationViewModel = operationViewModel
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<*>.slideInTransition(
    towards: AnimatedContentTransitionScope.SlideDirection
): EnterTransition {
    return slideIntoContainer(
        towards = towards,
        animationSpec = tween(300)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<*>.slideOutTransition(
    towards: AnimatedContentTransitionScope.SlideDirection
): ExitTransition {
    return slideOutOfContainer(
        towards = towards,
        animationSpec = tween(300)
    )
}