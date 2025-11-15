package br.com.chase.ui.screens.route

import br.com.chase.data.model.RouteRequest
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser

data class RouteState (
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val isConnected: Boolean = true,
    val isRecording: Boolean = false,

    // --- campos da rota
    val route: RouteRequest? = null,

    val name: String = "Sem nome",
    val description: String = "Sem descrição",

    val distance: Double = 0.0,
    val time: Long = 0L,

    val points: List<LatLng> = emptyList(),
    val startLocation: LatLng? = points.firstOrNull(),
    val endLocation: LatLng? = points.lastOrNull(),
    )