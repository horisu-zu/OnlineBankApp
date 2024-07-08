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
            enterTransition = { slideTransitionEnter(SlideDirection.Left) },
            exitTransition = { slideTransitionExit(SlideDirection.Left) },
            popEnterTransition = { slideTransitionEnter(SlideDirection.Right) },
            popExitTransition = { slideTransitionExit(SlideDirection.Right) }
        ) {
            ServicesListScreen(navController, operationViewModel)
        }

        composable(
            route = "operationList/{typeId}",
            enterTransition = { slideTransitionEnter(SlideDirection.Left) },
            exitTransition = { slideTransitionExit(SlideDirection.Left) },
            popEnterTransition = { slideTransitionEnter(SlideDirection.Right) },
            popExitTransition = { slideTransitionExit(SlideDirection.Right) }
        ) { backStackEntry ->
            OperationListScreen(
                typeId = backStackEntry.arguments?.getString("typeId") ?: "",
                operationViewModel = operationViewModel
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun slideTransitionEnter(
    towards: SlideDirection
): EnterTransition {
    return when (towards) {
        SlideDirection.Left -> slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
        SlideDirection.Right -> slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun slideTransitionExit(
    towards: SlideDirection
): ExitTransition {
    return when (towards) {
        SlideDirection.Left -> slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
        SlideDirection.Right -> slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
    }
}

enum class SlideDirection {
    Left, Right
}