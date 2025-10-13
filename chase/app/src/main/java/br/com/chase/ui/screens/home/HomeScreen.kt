package br.com.chase.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.chase.ui.theme.Poppins
import br.com.chase.ui.theme.PrimaryRainbow
import br.com.chase.R
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                actions = {4
                    Button(
                        onClick = { /*abrir menu */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.menu),
                            contentDescription = "Menu",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
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
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(3.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Brush.linearGradient(colors = PrimaryRainbow), CircleShape)
                ) {
                    FloatingActionButton(
                        onClick = { /* add rota */ },
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        modifier = Modifier.size(56.dp),
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar rota")
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
            ){
                NavigationBarItem(
                    selected = selectedTab == "statistics",
                    onClick = { selectedTab = "statistics" },
                    icon = {
                        Image(
                            painter = if (selectedTab == "statistics")
                                painterResource(R.drawable.statistics_colorido)
                            else
                                painterResource(R.drawable.statistics_black),
                            contentDescription = "Estatísticas",
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    selected = selectedTab == "home",
                    onClick = { selectedTab = "home" },
                    icon = {
                        Image(
                            painter = if (selectedTab == "home")
                                painterResource(R.drawable.maps_colorido)
                            else
                                painterResource(R.drawable.maps_black),
                            contentDescription = "Home",
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
                NavigationBarItem(
                    selected = selectedTab == "profile",
                    onClick = { selectedTab = "profile" },
                    icon = {
                        Image(
                            painter = if (selectedTab == "profile")
                                painterResource(R.drawable.user_colorido)
                            else
                                painterResource(R.drawable.user_black),
                            contentDescription = "Perfil",
                            modifier = Modifier.size(25.dp)

                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
            }
        }
    ) { paddingValues ->
        if (selectedTab == "home") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Bem-vindo à Home!")
            }
        } else if (selectedTab == "statistcs") {

        } else {

        }
    }
}



