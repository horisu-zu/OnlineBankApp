package com.example.onlinebankapp.domain.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.navigation.NavigationItem
import com.example.onlinebankapp.domain.presentation.viewmodel.user.UserViewModel
import com.example.onlinebankapp.domain.util.Resource
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainNavigationDrawer(
    data: List<NavigationItem>,
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: UserViewModel,
    parentNavController: NavController
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val userState by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerContainerColor = SlightlyGrey,
        drawerShape = RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp)
    ) {
        when (val state = userState) {
            is Resource.Loading -> {
                //CircularProgressIndicator()
            }
            is Resource.Success -> {
                val userData = state.data
                if (userData != null) {
                    Log.d("MainNavigationDrawer",
                        "Calling UserInfoSection with: userName=${userData.userName}, " +
                                "phoneNumber=${userData.phoneNumber}")
                    UserInfoSection(
                        avatarResId = R.drawable.ic_person,
                        userName = userData.userName,
                        phoneNumber = userData.phoneNumber ?: "No phone number"
                    )
                } else {
                    Log.d("MainNavigationDrawer", "UserData is null")
                }
            }
            is Resource.Error -> {
                state.message?.let { Log.e("Error", it) }
            }
        }

        NavigationItemsCard(data, selectedItemIndex) { index ->
            selectedItemIndex = index
            scope.launch { drawerState.close() }
        }

        LogoutCard {
            viewModel.logout()
            scope.launch {
                drawerState.close()
                parentNavController.navigate("auth") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }
    }

    /*LaunchedEffect(logoutState) {
        when (logoutState) {
            is Resource.Success -> {
                rootNavController.navigate("auth") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
            is Resource.Error -> {
            }
            is Resource.Loading -> {
            }
        }
    }*/
}

@Composable
private fun NavigationItemsCard(
    data: List<NavigationItem>,
    selectedItemIndex: Int,
    onItemClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = AnotherGray)
    ) {
        Column {
            data.forEachIndexed { index, item ->
                NavigationItem(item, index == selectedItemIndex) {
                    onItemClick(index)
                }
            }
        }
    }
}

@Composable
private fun NavigationItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = item.title, fontSize = 14.sp, color = Color.DarkGray) },
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                painter = item.icon,
                contentDescription = item.title,
                tint = Color.DarkGray
            )
        },
        badge = {
            item.badgeCount?.let {
                Text(text = it.toString(), fontSize = 14.sp, color = Color.DarkGray)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.LightGray,
            unselectedContainerColor = Color.Transparent
        )
    )
}

@Composable
private fun LogoutCard(onLogout: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AnotherGray)
    ) {
        NavigationDrawerItem(
            label = { Text("Logout", fontSize = 14.sp, color = Color.Red) },
            selected = false,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Logout",
                    tint = Color.Red
                )
            },
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun UserInfoSection(
    avatarResId: Int,
    userName: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    Log.d("UserInfoSection", "Composing with: userName=$userName, " +
            "phoneNumber=$phoneNumber")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = avatarResId),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = userName.takeIf { it.isNotBlank() } ?: "No name",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = phoneNumber.takeIf { it.isNotBlank() } ?: "...",
                fontSize = 14.sp
            )
        }
    }
}