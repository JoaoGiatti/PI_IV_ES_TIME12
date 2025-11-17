package br.com.chase.ui.screens.feed

import br.com.chase.data.model.RouteResponse
import br.com.chase.data.model.UserResponse
import com.google.firebase.auth.FirebaseUser

// Estado da tela de Feed, que contém a lista de Rotas
data class FeedState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val userData: UserResponse? = null,
    val isConnected: Boolean = true,
    val routes: List<RouteResponse> = emptyList(),
    val errorMessage: String? = null
)