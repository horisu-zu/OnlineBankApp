package com.example.onlinebankapp.domain.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.onlinebankapp.R
import com.example.onlinebankapp.ui.theme.Purple
import com.example.onlinebankapp.ui.theme.SlightlyGrey

@Composable
fun BottomNavigationMenu(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Services,
        BottomNavItem.History
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = SlightlyGrey
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Gray,
                    selectedTextColor = Color.Gray,
                    indicatorColor = SlightlyGrey,
                    unselectedIconColor = Color.DarkGray,
                    unselectedTextColor = Color.DarkGray
                )
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val label: String, val icon: Int) {
    object Home : BottomNavItem(
        "home_screen",
        "Home",
        R.drawable.ic_home
    )
    object History : BottomNavItem(
        "history_screen",
        "History",
        R.drawable.ic_history
    )
    object Services : BottomNavItem(
        "services_screen",
        "Services",
        R.drawable.ic_services
    )
}