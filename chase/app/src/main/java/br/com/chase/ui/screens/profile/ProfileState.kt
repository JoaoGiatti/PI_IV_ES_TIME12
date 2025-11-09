package br.com.chase.ui.screens.profile


import br.com.chase.data.api.RouteResponse
import br.com.chase.data.api.UserResponse
import com.google.firebase.auth.FirebaseUser

data class ProfileState (
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val userData: UserResponse? = null,
    val routes: List<RouteResponse> = emptyList(),
    val errorMessage: String? = null,
    val isConnected: Boolean = true,
    val editing: Boolean = false,
    val showDialog: Boolean = false,
    val editingBio: String = ""
)