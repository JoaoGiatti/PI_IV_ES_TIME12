package br.com.chase.ui.screens.route

import com.google.firebase.auth.FirebaseUser

data class RouteState (
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val isConnected: Boolean = true,
)