package br.com.chase.ui.screens.route

import br.com.chase.data.model.RouteRequest
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser

data class RouteState (
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val isConnected: Boolean = true,
    val route: RouteRequest? = null,

    // --- campos da rota ---
    val isRecording: Boolean = false,
    val path: List<LatLng> = emptyList(),
    val distanceMeters: Double = 0.0,
    val timeOfRoute: Long = 0L
)