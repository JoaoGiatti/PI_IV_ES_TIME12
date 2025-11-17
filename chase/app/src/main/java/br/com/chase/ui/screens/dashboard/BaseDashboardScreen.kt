package br.com.chase.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.R
import br.com.chase.ui.components.NoInternetBanner
import br.com.chase.ui.screens.feed.FeedScreen
import br.com.chase.ui.screens.profile.ProfileScreen
import br.com.chase.ui.screens.route.RouteScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDashboardScreen(
    viewModel: BaseDashboardViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = state.selectedTab,
        pageCount = { 3 }
    )

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
                                    painterResource(R.drawable.maps_colorido)
                                else painterResource(R.drawable.maps_black),
                                contentDescription = "Route",
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
                                    painterResource(R.drawable.statistics_colorido)
                                else painterResource(R.drawable.statistics_black),
                                contentDescription = "Feed",
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
            ) { page ->
                when (page) {
                    0 -> RouteScreen()
                    1 -> FeedScreen()
                    2 -> ProfileScreen()
                }
            }
        }
        if (!state.isConnected) {
            NoInternetBanner()
        }
    }
}
