package com.example.onlinebankapp.domain.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.onlinebankapp.R

data class NavigationItem(
    val title: String,
    val icon: Painter,
    val badgeCount: Int? = null
)

@Composable
fun NavigationItemList(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = "Settings",
            icon = painterResource(id = R.drawable.ic_settings)
        ),
        NavigationItem(
            title = "Rules and Info",
            icon = painterResource(id = R.drawable.ic_info),
            badgeCount = 13
        ),
        NavigationItem(
            title = "What's new",
            icon = painterResource(id = R.drawable.ic_update)
        )
    )
}