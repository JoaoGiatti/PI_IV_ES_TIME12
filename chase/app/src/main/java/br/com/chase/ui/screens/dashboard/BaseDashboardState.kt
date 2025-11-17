package br.com.chase.ui.screens.dashboard

import com.google.firebase.auth.FirebaseUser

data class BaseDashboardState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val isConnected: Boolean = true,
    val selectedTab: Int = 1,
    val topBarVisible: Boolean = true
)
