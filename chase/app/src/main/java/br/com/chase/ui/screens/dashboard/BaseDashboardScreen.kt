package br.com.chase.ui.screens.dashboard

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.R
import br.com.chase.ui.components.NoInternetBanner
import br.com.chase.ui.screens.profile.ProfileScreen
import br.com.chase.ui.screens.route.RouteData
import br.com.chase.ui.screens.route.RouteScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDashboardScreen(
    viewModel: BaseDashboardViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = state.selectedTab,
        pageCount = { 3 }
    )

    LaunchedEffect(state.topBarVisible) {
        if (state.topBarVisible) {
            delay(8000)
            viewModel.setTopBarVisible(false)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.selectTab(pagerState.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = state.selectedTab == 0,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                        icon = {
                            Image(
                                painter = if (state.selectedTab == 0)
                                    painterResource(R.drawable.statistics_colorido)
                                else painterResource(R.drawable.statistics_black),
                                contentDescription = "Feed",
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = state.selectedTab == 1,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                        icon = {
                            Image(
                                painter = if (state.selectedTab == 1)
                                    painterResource(R.drawable.maps_colorido)
                                else painterResource(R.drawable.maps_black),
                                contentDescription = "Route",
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = state.selectedTab == 2,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } },
                        icon = {
                            Image(
                                painter = if (state.selectedTab == 2)
                                    painterResource(R.drawable.user_colorido)
                                else painterResource(R.drawable.user_black),
                                contentDescription = "Profile",
                                modifier = Modifier.size(25.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            if (dragAmount > 10) viewModel.setTopBarVisible(true)
                            if (dragAmount < -10) viewModel.setTopBarVisible(false)
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { viewModel.setTopBarVisible(true) })
                    }
            ) { page ->
                when (page) {
                    0 -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Bem-vindo ao Feed!")
                    }

                    1 -> RouteScreen()

                    2 -> ProfileScreen()
                }
            }
        }

        AnimatedVisibility(
            visible = state.topBarVisible,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it })
        ) {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.chase_logo_mais_nome),
                        contentDescription = "CHASE",
                        modifier = Modifier.size(width = 145.dp, height = 28.dp),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                modifier = Modifier.shadow(3.dp)
            )
        }

        if (!state.isConnected) {
            NoInternetBanner()
        }
    }
}
