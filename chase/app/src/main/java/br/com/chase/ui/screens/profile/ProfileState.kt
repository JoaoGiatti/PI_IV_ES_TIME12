package br.com.chase.ui.screens.profile


import br.com.chase.data.api.RouteResponse
import com.google.firebase.auth.FirebaseUser

data class ProfileState (
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val isConnected: Boolean = true,
    val bio: String = "",
    val editing: Boolean = false,
    val showDialog: Boolean = false,
    val routes: List<RouteResponse> = emptyList()
)