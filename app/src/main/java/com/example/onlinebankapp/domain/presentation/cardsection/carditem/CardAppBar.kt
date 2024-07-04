package com.example.onlinebankapp.domain.presentation.cardsection.carditem

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.onlinebankapp.R
import com.example.onlinebankapp.domain.card.PaymentCardData
import com.example.onlinebankapp.domain.card.toColor
import com.example.onlinebankapp.domain.presentation.cardsection.getTextColorForBackground
import com.example.onlinebankapp.ui.theme.SlightlyGrey
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardAppBar(
    paymentCardData: PaymentCardData,
    content: @Composable () -> Unit
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as? Activity

    var cardHeaderHeightPx by remember { mutableFloatStateOf(0f) }

    val isScrolled = scrollState.firstVisibleItemScrollOffset > 0

    val showButton by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlightlyGrey)
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                CardHeader(
                    paymentCardData = paymentCardData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .onSizeChanged { size ->
                            cardHeaderHeightPx = size.height.toFloat()
                        }
                )
            }
            item {
                content()
            }
        }

        AnimatedVisibility(visible = showButton) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollToItem(
                            index = 0
                        )
                    }
                }
            )
        }

        MainAppBar(
            paymentCardData = paymentCardData,
            isScrolled = isScrolled,
            onBackClick = { activity?.onBackPressed() },
            onSettingsClick = { /*onSettingsClick*/ },
            onDetailsClick = { /*onDetailsClick*/ }
        )
    }
}

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 36.dp, end = 36.dp),
        Alignment.BottomEnd
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .shadow(elevation = 6.dp, shape = CircleShape)
                .clip(shape = CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.DarkGray
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Arrow Up",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    paymentCardData: PaymentCardData,
    isScrolled: Boolean,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    val tintColor = getTextColorForBackground(paymentCardData.cardColor.toColor())
    val barColor = getElevationColor(paymentCardData.cardColor.toColor())

    TopAppBar(
        title = {
            Text(text = if (isScrolled) paymentCardData.cardName else "", color = tintColor) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = tintColor
                )
            }
        },
        actions = {
            Row {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = tintColor
                    )
                }
                IconButton(onClick = onDetailsClick) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info/Details",
                        tint = tintColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isScrolled) barColor else Color.Transparent,
            scrolledContainerColor = barColor
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    scrollState: LazyListState,
    barColor: Color,
    onScrollToTop: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSortClick: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = "",
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search") },
                modifier = Modifier.fillMaxWidth(0.7f),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onScrollToTop) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to Top",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = onSortClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort),
                    contentDescription = "Sort",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = barColor,
            scrolledContainerColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
