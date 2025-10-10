package br.com.chase.ui.screens.home

import com.google.firebase.auth.FirebaseUser

data class HomeState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val isConnected: Boolean = true
    // TODO: Adicionar outros possiveis estados da tela
)
