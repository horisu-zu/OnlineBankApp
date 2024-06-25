package com.example.onlinebankapp.domain.presentation

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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.navigation.NavigationItem
import com.example.onlinebankapp.ui.theme.AnotherGray
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainNavigationDrawer(
    data: List<NavigationItem>,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerContainerColor = SlightlyGrey,
        drawerShape = RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp)
    ) {
        UserInfoSection(
            avatarResId = R.drawable.ic_person,
            userName = "UserName",
            phoneNumber = "+380679761684"
        )

        NavigationItemsCard(data, selectedItemIndex) { index ->
            selectedItemIndex = index
            scope.launch { drawerState.close() }
        }

        LogoutCard { scope.launch { drawerState.close() } }
    }
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
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
            modifier = Modifier.padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = phoneNumber,
                fontSize = 14.sp
            )
        }
    }
}